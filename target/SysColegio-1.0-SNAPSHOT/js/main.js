import { apiCall, apiGet} from "../js/transport/api.js"


const $ = (id) => document.getElementById(id);

function agregarRows(datos, columnas) {
    const tbody = $("table-body-alumno");
    const fragment = document.createDocumentFragment();

    const lista = Array.isArray(datos) ? datos : [datos];

    lista.forEach(obj => {
        const fila = document.createElement("tr");

        columnas.forEach(col => {
            const td = document.createElement("td");
            td.textContent = obj[col];
            fila.appendChild(td);
        });

        fragment.appendChild(fila);
    });

    tbody.appendChild(fragment);
}


if ($("login")) {
    $("login").addEventListener("click", async () => {

        const user = $("username").value.trim();
        const pass = $("pass").value.trim();

        if (!user || !pass) {
            $("mensaje").textContent = "Complete todos los campos.";
            return;
        }

        let payload = { username: user, password: pass }

        try {

            const response = await apiCall("login", payload);

            if (response.resultado === "ok") {
                // Redirigir al dashboard
                window.location.href = "app/dashboard.html";
            } else {
                $("mensaje").textContent = response.mensaje || "Credenciales incorrectas.";
            }

        } catch (error) {
            console.error(error);
            $("mensaje").textContent = "Error de conexión.";
        }
    });
}


if ($("search")) {
    $("search").addEventListener("click", async () => {
        const fila = $("table-body-alumno").querySelector("tr");
        if (fila) {
            fila.remove();
        }

        let dni = $("dni");

        const dniValid = /^\d{8}$/.test(dni.value.trim());

        if (!dniValid) {
            dni.value = '';
            dni.placeholder = 'Formato de DNI invalido.';
            return;
        }

        let payload = { dni: dni.value.trim() };
        

        try {
            const resp = await apiCall("alumno/buscar", payload);
            if (resp.resultado === "ok") {
                const columnas = ["nombre", "correo", "celular", "sexo"];
                agregarRows(resp, columnas);
            } else {
                $("mensaje").textContent = resp.mensaje;
            }
        } catch (error) {
            console.error(error);
            $("mensaje").textContent = "Error de conexión.";
        }
    });
}


if ($("registrar-alumno")) {
    $("registrar-alumno").addEventListener("click", async(e) => {
        e.preventDefault();
        const form = $("form-registro");
        const formData = new FormData(form);

        const dni = formData.get("dni");
        const appa = formData.get("appa");
        const apma = formData.get("apma");
        const nomb = formData.get("nomb");
        const corr = formData.get("corr");
        const cel = formData.get("cel");
        const sexo = formData.get("sexo");

        if (!form.checkValidity()) {
            alert("Por favor, complete todos los campos requeridos con el formato correcto.");
            return;
        }

        let payload = {
            dni: dni,
            appa: appa,
            apma: apma,
            nomb: nomb,
            corr: corr,
            cel: cel,
            sexo: sexo
        };

        try {
            const resp = await apiCall("alumno/registrar", payload);
            $("mensaje").textContent = resp.mensaje;
        } catch (error) {
            console.error(error);
            $("mensaje").textContent = "Error de conexión.";
        }
    });
}


if($("listar-aulas")){
    $("listar-aulas").addEventListener("click", async () => {
        try {
            const resp = await apiGet("aula/listar");
            if (resp.ok) {
                const columnas = ["codiAula","detalle"];
                agregarRows(resp, columnas);
            } else {
                $("mensaje").textContent = resp.mensaje;
            }
        } catch (error) {
            console.error(error);
            $("mensaje").textContent = "Error de conexión.";
        }
    });
}



