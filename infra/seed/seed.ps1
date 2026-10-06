<#
.SYNOPSIS
  Puebla una base RevTech VACIA llamando a la API REST del sistema en ejecucion.

.DESCRIPTION
  Actua como el administrador: crea roles, usuarios del personal, clientes, vehiculos e inspecciones
  (en todos sus estados) usando solo los endpoints /api/**. Los datos son fijos (sin azar), asi que
  cada ejecucion sobre una base vacia produce el mismo resultado y el volcado es reproducible.

  Despues se vuelca con `mise run db:dump-seed` a infra/mysql/seed-data.sql.

.EXAMPLE
  ./infra/seed/seed.ps1
  ./infra/seed/seed.ps1 -BaseUrl http://localhost:8000
#>
param(
    [string]$BaseUrl = 'http://localhost:8000'
)

$ErrorActionPreference = 'Stop'

function Invoke-Api {
    param([string]$Method, [string]$Path, $Body = $null)
    $params = @{ Method = $Method; Uri = "$BaseUrl$Path"; ContentType = 'application/json; charset=utf-8' }
    if ($null -ne $Body) {
        # Bytes UTF-8 explicitos: Windows PowerShell 5.1 no codifica bien los acentos por defecto.
        $params.Body = [System.Text.Encoding]::UTF8.GetBytes(($Body | ConvertTo-Json -Depth 5))
    }
    try {
        Invoke-RestMethod @params
    } catch {
        $detalle = $_.ErrorDetails.Message
        throw "$Method $Path fallo: $($_.Exception.Message) $detalle"
    }
}

function Post($path, $body) { Invoke-Api 'POST' $path $body }
function Put($path) { Invoke-Api 'PUT' $path }

$todasAprobadas = @{ FRENOS = 'APROBADO'; DIRECCION = 'APROBADO'; SUSPENSION = 'APROBADO'
                     LUCES = 'APROBADO'; NEUMATICOS = 'APROBADO'; EMISIONES = 'APROBADO' }

function Registrar-Pruebas($idInspeccion, $resultados) {
    foreach ($prueba in $resultados.Keys) {
        Post "/api/inspecciones/$idInspeccion/pruebas" @{ prueba = $prueba; resultado = $resultados[$prueba] } | Out-Null
    }
}

function Nueva-Inspeccion($vehiculo, $inspector, $supervisor, $origen = $null) {
    $body = @{ idVehiculo = $vehiculo; idInspector = $inspector; idSupervisor = $supervisor }
    if ($origen) { $body.tipoInspeccion = 'REINSPECCION'; $body.idInspeccionOrigen = $origen }
    (Post '/api/inspecciones' $body).idInspeccion
}

# Recorre el ciclo: iniciar, registrar pruebas y finalizar (opcional).
function Completar-Inspeccion($id, $resultados, [switch]$Finalizar, $observaciones = $null) {
    Put "/api/inspecciones/$id/iniciar" | Out-Null
    if ($resultados) { Registrar-Pruebas $id $resultados }
    if ($Finalizar) {
        $body = if ($observaciones) { @{ observaciones = $observaciones } } else { @{} }
        Invoke-Api 'PUT' "/api/inspecciones/$id/finalizar" $body | Out-Null
    }
}

Write-Host "Sembrando $BaseUrl ..."

# 1. Roles (solo los del personal; los clientes no son usuarios del sistema)
$roles = @{}
$permisosPorRol = [ordered]@{
    ROLE_ADMIN         = @('USUARIOS_GESTIONAR', 'ROLES_GESTIONAR', 'CLIENTES_GESTIONAR', 'INSPECCIONES_GESTIONAR')
    ROLE_RECEPCIONISTA = @('CLIENTES_GESTIONAR', 'VEHICULOS_GESTIONAR', 'INSPECCIONES_REGISTRAR')
    ROLE_INSPECTOR     = @('INSPECCIONES_INICIAR', 'PRUEBAS_REGISTRAR', 'INSPECCIONES_FINALIZAR')
    ROLE_SUPERVISOR    = @('INSPECCIONES_CONSULTAR', 'INSPECCIONES_REVISAR')
}
foreach ($nombre in $permisosPorRol.Keys) {
    $roles[$nombre] = (Post '/api/roles' @{ nombreRol = $nombre; permisos = $permisosPorRol[$nombre] }).idRol
}

# 2. Usuarios del personal
function Nuevo-Usuario($username, $rol) {
    (Post '/api/usuarios' @{ username = $username; password = 'RevTech2026!'; rolActivoId = $roles[$rol] }).idUsuario
}
$admin = Nuevo-Usuario 'admin' 'ROLE_ADMIN'
$recepcion1 = Nuevo-Usuario 'recepcion.lucia' 'ROLE_RECEPCIONISTA'
$recepcion2 = Nuevo-Usuario 'recepcion.marco' 'ROLE_RECEPCIONISTA'
$insp1 = Nuevo-Usuario 'inspector.juan' 'ROLE_INSPECTOR'
$insp2 = Nuevo-Usuario 'inspector.rosa' 'ROLE_INSPECTOR'
$insp3 = Nuevo-Usuario 'inspector.pedro' 'ROLE_INSPECTOR'
$sup = Nuevo-Usuario 'supervisor.elena' 'ROLE_SUPERVISOR'

# 3. Clientes
$clientesDatos = @(
    @('45871236', 'Carlos Mendoza Rojas'), @('40125987', 'Ana Torres Quispe'),
    @('72458963', 'Luis Fernandez Soto'), @('08147529', 'Maria Gutierrez Paredes'),
    @('46321587', 'Jose Huaman Ccori'), @('70236914', 'Patricia Salazar Vega'),
    @('10458723', 'Transportes Andinos SAC'), @('44785210', 'Diego Ramirez Luna')
)
$clientes = foreach ($c in $clientesDatos) {
    (Post '/api/clientes' @{ docIdent = $c[0]; nombre = $c[1] }).idCliente
}

# 4. Vehiculos (cliente, placa, categoria, marca, modelo, anio)
$vehiculosDatos = @(
    @(0, 'ABC-123', 'M1', 'Toyota', 'Corolla', 2019), @(0, 'ABD-456', 'L', 'Honda', 'CB190R', 2022),
    @(1, 'BCD-234', 'M1', 'Hyundai', 'Accent', 2016), @(2, 'CDE-345', 'N1', 'Nissan', 'Frontier', 2018),
    @(3, 'DEF-456', 'M1', 'Kia', 'Rio', 2020), @(4, 'EFG-567', 'M2', 'Toyota', 'Hiace', 2015),
    @(5, 'FGH-678', 'M1', 'Suzuki', 'Swift', 2012), @(6, 'GHI-789', 'N3', 'Volvo', 'FH16', 2017),
    @(6, 'HIJ-890', 'M3', 'Mercedes-Benz', 'O500', 2014), @(6, 'IJK-901', 'O2', 'Randon', 'SR', 2010),
    @(7, 'JKL-012', 'M1', 'Mazda', 'CX-5', 2021), @(7, 'KLM-123', 'N2', 'Hino', '300', 2008)
)
$vehiculos = foreach ($v in $vehiculosDatos) {
    (Post '/api/vehiculos' @{
        clienteId = $clientes[$v[0]]; placa = $v[1]; categoria = $v[2]
        marca = $v[3]; modelo = $v[4]; anioFabricacion = $v[5]
    }).idVehiculo
}

# 5. Inspecciones en todos sus estados
$observadoFrenos = $todasAprobadas.Clone(); $observadoFrenos.FRENOS = 'OBSERVADO'
$rechazadoLuces = $todasAprobadas.Clone(); $rechazadoLuces.LUCES = 'RECHAZADO'; $rechazadoLuces.NEUMATICOS = 'OBSERVADO'
$rechazadoEmisiones = $todasAprobadas.Clone(); $rechazadoEmisiones.EMISIONES = 'RECHAZADO'

# FINALIZADA / APTO (certificado)
foreach ($par in @(@(0, $insp1), @(1, $insp2), @(2, $insp3))) {
    $id = Nueva-Inspeccion $vehiculos[$par[0]] $par[1] $sup
    Completar-Inspeccion $id $todasAprobadas -Finalizar
}

# FINALIZADA / OBSERVADO (acta) y su reinspeccion
$obs1 = Nueva-Inspeccion $vehiculos[3] $insp1 $sup
Completar-Inspeccion $obs1 $observadoFrenos -Finalizar -observaciones 'Frenos con eficiencia por debajo del minimo; reparar y reinspeccionar.'
$rein1 = Nueva-Inspeccion $vehiculos[3] $insp1 $sup $obs1
Completar-Inspeccion $rein1 $todasAprobadas -Finalizar                       # reinspeccion APTA

$obs2 = Nueva-Inspeccion $vehiculos[4] $insp2 $sup
Completar-Inspeccion $obs2 $rechazadoLuces -Finalizar -observaciones 'Faros desalineados y neumaticos con desgaste irregular.'
$rein2 = Nueva-Inspeccion $vehiculos[4] $insp2 $sup $obs2
Completar-Inspeccion $rein2 $null                                            # reinspeccion EN_PROCESO

$obs3 = Nueva-Inspeccion $vehiculos[5] $insp3 $null                          # sin supervisor
Completar-Inspeccion $obs3 $rechazadoEmisiones -Finalizar -observaciones 'Emisiones sobre el limite permitido.'

# EN_PROCESO (con y sin pruebas)
$enProceso1 = Nueva-Inspeccion $vehiculos[6] $insp1 $sup
Completar-Inspeccion $enProceso1 @{ FRENOS = 'APROBADO'; LUCES = 'APROBADO' }
$enProceso2 = Nueva-Inspeccion $vehiculos[7] $insp3 $sup
Completar-Inspeccion $enProceso2 $null

# REGISTRADA (sin iniciar)
Nueva-Inspeccion $vehiculos[8] $insp2 $sup | Out-Null
Nueva-Inspeccion $vehiculos[9] $insp3 $null | Out-Null

# Los vehiculos 10 y 11 quedan sin inspecciones a proposito.

Write-Host "Listo: $($roles.Count) roles, 7 usuarios, $($clientes.Count) clientes, $($vehiculos.Count) vehiculos, 12 inspecciones."
Write-Host "Siguiente paso: mise run db:dump-seed"
