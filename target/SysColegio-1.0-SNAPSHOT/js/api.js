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

    let payload = {dni:dni};

    try {
        const resp = await apiCall("alumno/buscar",payload);
        console.log(resp);
        if (resp.resultado === "ok") {
            console.log(resp)
        } else {
            $("mensaje").textContent = response.mensaje;
        }
    } catch (error) {
        console.error(error);
        $("mensaje").textContent = "Error de conexión.";
    }
});

