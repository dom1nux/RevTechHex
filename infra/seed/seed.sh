#!/usr/bin/env bash
# Puebla una base RevTech VACIA con curl contra la API en ejecucion (variante de seed.ps1, mismos datos).
# Actua como el administrador (aun no hay autenticacion). Uso: ./infra/seed/seed.sh [http://localhost:8000]
set -euo pipefail

BASE_URL="${1:-http://localhost:8000}"

# api METODO RUTA [JSON]: imprime el cuerpo de la respuesta; aborta si el status no es 2xx.
api() {
    local metodo=$1 ruta=$2 cuerpo=${3:-} out status
    if [ -n "$cuerpo" ]; then
        out=$(curl -sS -w '\n%{http_code}' -X "$metodo" "$BASE_URL$ruta" -H 'Content-Type: application/json' -d "$cuerpo")
    else
        out=$(curl -sS -w '\n%{http_code}' -X "$metodo" "$BASE_URL$ruta")
    fi
    status=${out##*$'\n'}
    if [[ ! $status =~ ^2 ]]; then
        echo "$metodo $ruta fallo ($status): ${out%$'\n'*}" >&2
        exit 1
    fi
    printf '%s' "${out%$'\n'*}"
}

# id CAMPO: extrae un campo numerico del JSON recibido por stdin.
id() { sed -E "s/.*\"$1\":([0-9]+).*/\1/"; }

post() { api POST "$@"; }

echo "Sembrando $BASE_URL ..."

# 1. Roles (solo los del personal; los clientes no son usuarios del sistema)
declare -A ROL
rol() { ROL[$1]=$(post /api/roles "{\"nombreRol\":\"$1\",\"permisos\":[$2]}" | id idRol); }
rol ROLE_ADMIN '"USUARIOS_GESTIONAR","ROLES_GESTIONAR","CLIENTES_GESTIONAR","INSPECCIONES_GESTIONAR"'
rol ROLE_RECEPCIONISTA '"CLIENTES_GESTIONAR","VEHICULOS_GESTIONAR","INSPECCIONES_REGISTRAR"'
rol ROLE_INSPECTOR '"INSPECCIONES_INICIAR","PRUEBAS_REGISTRAR","INSPECCIONES_FINALIZAR"'
rol ROLE_SUPERVISOR '"INSPECCIONES_CONSULTAR","INSPECCIONES_REVISAR"'

# 2. Usuarios del personal
usuario() { post /api/usuarios "{\"username\":\"$1\",\"password\":\"RevTech2026!\",\"rolActivoId\":${ROL[$2]}}" | id idUsuario; }
usuario admin ROLE_ADMIN >/dev/null
usuario recepcion.lucia ROLE_RECEPCIONISTA >/dev/null
usuario recepcion.marco ROLE_RECEPCIONISTA >/dev/null
INSP1=$(usuario inspector.juan ROLE_INSPECTOR)
INSP2=$(usuario inspector.rosa ROLE_INSPECTOR)
INSP3=$(usuario inspector.pedro ROLE_INSPECTOR)
SUP=$(usuario supervisor.elena ROLE_SUPERVISOR)

# 3. Clientes
CLI=()
cliente() { CLI+=("$(post /api/clientes "{\"docIdent\":\"$1\",\"nombre\":\"$2\"}" | id idCliente)"); }
cliente 45871236 'Carlos Mendoza Rojas'
cliente 40125987 'Ana Torres Quispe'
cliente 72458963 'Luis Fernandez Soto'
cliente 08147529 'Maria Gutierrez Paredes'
cliente 46321587 'Jose Huaman Ccori'
cliente 70236914 'Patricia Salazar Vega'
cliente 10458723 'Transportes Andinos SAC'
cliente 44785210 'Diego Ramirez Luna'

# 4. Vehiculos: indice_cliente placa categoria marca modelo anio
VEH=()
vehiculo() {
    VEH+=("$(post /api/vehiculos "{\"clienteId\":${CLI[$1]},\"placa\":\"$2\",\"categoria\":\"$3\",\"marca\":\"$4\",\"modelo\":\"$5\",\"anioFabricacion\":$6}" | id idVehiculo)")
}
vehiculo 0 ABC-123 M1 Toyota Corolla 2019
vehiculo 0 ABD-456 L Honda CB190R 2022
vehiculo 1 BCD-234 M1 Hyundai Accent 2016
vehiculo 2 CDE-345 N1 Nissan Frontier 2018
vehiculo 3 DEF-456 M1 Kia Rio 2020
vehiculo 4 EFG-567 M2 Toyota Hiace 2015
vehiculo 5 FGH-678 M1 Suzuki Swift 2012
vehiculo 6 GHI-789 N3 Volvo FH16 2017
vehiculo 6 HIJ-890 M3 Mercedes-Benz O500 2014
vehiculo 6 IJK-901 O2 Randon SR 2010
vehiculo 7 JKL-012 M1 Mazda CX-5 2021
vehiculo 7 KLM-123 N2 Hino 300 2008

# 5. Inspecciones en todos sus estados
# nueva VEHICULO INSPECTOR SUPERVISOR|null [ORIGEN]
nueva() {
    local sup=$3 cuerpo
    cuerpo="{\"idVehiculo\":$1,\"idInspector\":$2,\"idSupervisor\":$sup"
    [ -n "${4:-}" ] && cuerpo+=",\"tipoInspeccion\":\"REINSPECCION\",\"idInspeccionOrigen\":$4"
    post /api/inspecciones "$cuerpo}" | id idInspeccion
}
iniciar() { api PUT "/api/inspecciones/$1/iniciar" >/dev/null; }
prueba() { post "/api/inspecciones/$1/pruebas" "{\"prueba\":\"$2\",\"resultado\":\"$3\"}" >/dev/null; }
finalizar() { local vacio="{}"; api PUT "/api/inspecciones/$1/finalizar" "${2:-$vacio}" >/dev/null; }
# pruebas ID FRENOS DIRECCION SUSPENSION LUCES NEUMATICOS EMISIONES (veredictos en ese orden)
pruebas() {
    local id=$1; shift
    local nombres=(FRENOS DIRECCION SUSPENSION LUCES NEUMATICOS EMISIONES) i
    for i in 0 1 2 3 4 5; do prueba "$id" "${nombres[$i]}" "$1"; shift; done
}
OK=APROBADO

# FINALIZADA / APTO (certificado)
for par in "0 $INSP1" "1 $INSP2" "2 $INSP3"; do
    set -- $par
    ID=$(nueva "${VEH[$1]}" "$2" "$SUP"); iniciar "$ID"; pruebas "$ID" $OK $OK $OK $OK $OK $OK; finalizar "$ID"
done

# FINALIZADA / OBSERVADO (acta) y su reinspeccion
OBS1=$(nueva "${VEH[3]}" "$INSP1" "$SUP"); iniciar "$OBS1"; pruebas "$OBS1" OBSERVADO $OK $OK $OK $OK $OK
finalizar "$OBS1" '{"observaciones":"Frenos con eficiencia por debajo del minimo; reparar y reinspeccionar."}'
REIN1=$(nueva "${VEH[3]}" "$INSP1" "$SUP" "$OBS1"); iniciar "$REIN1"; pruebas "$REIN1" $OK $OK $OK $OK $OK $OK; finalizar "$REIN1"

OBS2=$(nueva "${VEH[4]}" "$INSP2" "$SUP"); iniciar "$OBS2"; pruebas "$OBS2" $OK $OK $OK RECHAZADO OBSERVADO $OK
finalizar "$OBS2" '{"observaciones":"Faros desalineados y neumaticos con desgaste irregular."}'
REIN2=$(nueva "${VEH[4]}" "$INSP2" "$SUP" "$OBS2"); iniciar "$REIN2"          # reinspeccion EN_PROCESO

OBS3=$(nueva "${VEH[5]}" "$INSP3" null); iniciar "$OBS3"; pruebas "$OBS3" $OK $OK $OK $OK $OK RECHAZADO   # sin supervisor
finalizar "$OBS3" '{"observaciones":"Emisiones sobre el limite permitido."}'

# EN_PROCESO (con y sin pruebas)
EP1=$(nueva "${VEH[6]}" "$INSP1" "$SUP"); iniciar "$EP1"; prueba "$EP1" FRENOS $OK; prueba "$EP1" LUCES $OK
EP2=$(nueva "${VEH[7]}" "$INSP3" "$SUP"); iniciar "$EP2"

# REGISTRADA (sin iniciar)
nueva "${VEH[8]}" "$INSP2" "$SUP" >/dev/null
nueva "${VEH[9]}" "$INSP3" null >/dev/null

# Los vehiculos 10 y 11 quedan sin inspecciones a proposito.
echo "Listo: 4 roles, 7 usuarios, ${#CLI[@]} clientes, ${#VEH[@]} vehiculos, 12 inspecciones."
echo "Siguiente paso: mise run db:dump-seed"
