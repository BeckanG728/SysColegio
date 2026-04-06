// Ejemplo de uso desde cualquier pagina:
// const resp = await apiCall('/ColegioWeb/app/BuscarAlumnoServlet', { dni: '40801418'});
// console.log(resp.nombre);

const $ = (id) => document.getElementById(id);

// Buscar alumno

$("search").addEventListener("click", async () =>{
    let dni = $("dni");

    const dniValid = /^\d{8}$/.test(dni.value.trim());

    if(!dniValid){
        dni.value = '';
        dni.placeholder = 'Formato de DNI invalido.';
        return;
    }

    let payload = {dni:dni.value.trim()};

    try {
        const resp = await apiCall("alumno/buscar",payload);
        console.log(resp);
        if (resp.resultado === "ok") {
            console.log(resp)
        } else {
            $("mensaje").textContent = resp.mensaje;
        }
    } catch (error) {
        console.error(error);
        $("mensaje").textContent = "Error de conexión.";
    }
});


/**
 * api.js
 * Punto único de lógica de negocio del frontend.
 * Detecta la página actual y activa los handlers correspondientes.
 * 
 * Orden de scripts en cada HTML:
 *   <script src="/js/cesar.js"></script>
 *   <script src="/js/codec.js"></script>
 *   <script src="/js/api.js"></script>
 */
 
// ─── Utilidad DOM ────────────────────────────────────────────────────────────

// ─── Helpers de UI ───────────────────────────────────────────────────────────
 
function setMensaje(id, texto, esError = false) {
    const el = $(id);
    if (!el) return;
    el.textContent = texto;
    el.style.color = esError ? "red" : "green";
}
 
function addRowTable(tbodyId, columnas) {
    const tbody = document.querySelector(`#${tbodyId} tbody`);
    if (!tbody) return;
    const fila = tbody.insertRow();
    columnas.forEach((val) => {
        const cel = fila.insertCell();
        cel.textContent = val;
    });
}
 
function clearTable(tbodyId) {
    const tbody = document.querySelector(`#${tbodyId} tbody`);
    if (tbody) tbody.innerHTML = "";
}
 
// ─── Módulo: Login (index.html) ───────────────────────────────────────────────
 
function initLogin() {
    $("login").addEventListener("click", async () => {
        const user = $("username").value.trim();
        const pass = $("pass").value.trim();
 
        if (!user || !pass) {
            setMensaje("mensaje", "Complete todos los campos.", true);
            return;
        }
 
        try {
            const { status, data } = await apiCall("login", { username: user, password: pass });
 
            if (data.resultado === "ok") {
                window.location.href = "app/dashboard.html";
            } else {
                setMensaje("mensaje", data.mensaje || "Credenciales incorrectas.", true);
            }
        } catch (error) {
            console.error(error);
            setMensaje("mensaje", "Error de conexión.", true);
        }
    });
}
 
// ─── Módulo: Buscar alumno (buscar-alumno.html) ───────────────────────────────
 
function initBuscarAlumno() {
    $("search").addEventListener("click", async () => {
        const dniInput = $("dni");
        const dniValor = dniInput.value.trim();
 
        if (!/^\d{8}$/.test(dniValor)) {
            dniInput.value = "";
            dniInput.placeholder = "Formato de DNI inválido.";
            return;
        }
 
        try {
            const { status, data } = await apiCall("alumno/buscar", { dni: dniValor });
 
            if (data.resultado === "ok") {
                clearTable("table-alumnos");
                const nombreCompleto = `${data.nombre}`;
                addRowTable("table-alumnos", [dniValor, nombreCompleto]);
                setMensaje("mensaje", "");
            } else {
                clearTable("table-alumnos");
                setMensaje("mensaje", data.mensaje || "Alumno no encontrado.", true);
            }
        } catch (error) {
            console.error(error);
            setMensaje("mensaje", "Error de conexión.", true);
        }
    });
}
 
// ─── Módulo: Dashboard (dashboard.html) ───────────────────────────────────────
// Aquí irá la lógica del listado general cuando el servlet esté implementado.
 
function initDashboard() {
    const btnListar = $("btnListar");
    if (!btnListar) return;
 
    btnListar.addEventListener("click", async () => {
        try {
            const { status, data } = await apiCall("app/alumno/listar", {});
 
            if (Array.isArray(data)) {
                clearTable("table-alumnos");
                data.forEach((alumno) => {
                    const nombre = `${alumno.nombre}`;
                    addRowTable("table-alumnos", [alumno.dni, nombre]);
                });
            } else {
                setMensaje("mensaje", data.mensaje || "Error al listar alumnos.", true);
            }
        } catch (error) {
            console.error(error);
        }
    });
}
 
// ─── Módulo: Registrar alumno (registrar-alumno.html) ─────────────────────────
// Esqueleto listo para cuando el servlet esté implementado.
 
function initRegistrarAlumno() {
    const btnRegistrar = $("btnRegistrar");
    if (!btnRegistrar) return;
 
    btnRegistrar.addEventListener("click", async () => {
        const payload = {
            dni:      $("dni")      ?.value.trim(),
            nombres:  $("nombres")  ?.value.trim(),
            appAlum:  $("appAlum")  ?.value.trim(),
            apmAlum:  $("apmAlum")  ?.value.trim(),
            correo:   $("correo")   ?.value.trim(),
            celular:  $("celular")  ?.value.trim(),
            sexo:     $("sexo")     ?.value,
        };
 
        if (!payload.dni || !payload.nombres || !payload.appAlum || !payload.apmAlum) {
            setMensaje("mensaje", "Complete los campos obligatorios.", true);
            return;
        }
 
        try {
            const { status, data } = await apiCall("app/alumno/registrar", payload);
 
            if (data.resultado === "ok") {
                setMensaje("mensaje", "Alumno registrado correctamente.");
            } else {
                setMensaje("mensaje", data.mensaje || "Error al registrar.", true);
            }
        } catch (error) {
            console.error(error);
            setMensaje("mensaje", "Error de conexión.", true);
        }
    });
}
 
// ─── Módulo: Matricular (matricular.html) ─────────────────────────────────────
 
function initMatricular() {
    const btnMatricular = $("btnMatricular");
    if (!btnMatricular) return;
 
    btnMatricular.addEventListener("click", async () => {
        const payload = {
            dniAlumno: $("dniAlumno")?.value.trim(),
            codiAula:  $("codiAula") ?.value,
        };
 
        if (!payload.dniAlumno || !payload.codiAula) {
            setMensaje("mensaje", "Seleccione alumno y aula.", true);
            return;
        }
 
        try {
            const { status, data } = await apiCall("app/matricula/registrar", payload);
 
            if (data.resultado === "ok") {
                setMensaje("mensaje", "Matrícula registrada correctamente.");
            } else {
                setMensaje("mensaje", data.mensaje || "Error al matricular.", true);
            }
        } catch (error) {
            console.error(error);
            setMensaje("mensaje", "Error de conexión.", true);
        }
    });
}
 
// ─── Módulo: Pagos (pagos.html) ───────────────────────────────────────────────
 
function initPagos() {
    const btnPagar = $("btnPagar");
    if (!btnPagar) return;
 
    btnPagar.addEventListener("click", async () => {
        const payload = {
            codiMatr: $("codiMatr")?.value,
            codiConc: $("codiConc")?.value,
            monto:    $("monto")   ?.value,
        };
 
        if (!payload.codiMatr || !payload.codiConc || !payload.monto) {
            setMensaje("mensaje", "Complete todos los campos.", true);
            return;
        }
 
        try {
            const { status, data } = await apiCall("app/pago/registrar", payload);
 
            if (data.resultado === "ok") {
                setMensaje("mensaje", "Pago registrado correctamente.");
            } else {
                setMensaje("mensaje", data.mensaje || "Error al registrar pago.", true);
            }
        } catch (error) {
            console.error(error);
            setMensaje("mensaje", "Error de conexión.", true);
        }
    });
}
 
// ─── Router: detecta la página y activa el módulo correcto ────────────────────
 
(function init() {
    const page = window.location.pathname.split("/").pop();
 
    const routes = {
        "index.html": initLogin,
        "":           initLogin,          // raíz del contexto
        "buscar-alumno.html":   initBuscarAlumno,
        "dashboard.html":       initDashboard,
        "registrar-alumno.html":initRegistrarAlumno,
        "matricular.html":      initMatricular,
        "pagos.html":           initPagos,
    };
 
    const handler = routes[page];
    if (handler) {
        handler();
    } else {
        console.warn(`api.js: no hay módulo registrado para "${page}"`);
    }
})();

