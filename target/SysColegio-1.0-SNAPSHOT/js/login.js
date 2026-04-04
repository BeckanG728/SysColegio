const $ = (id) => document.getElementById(id);

$("login").addEventListener("click", async () => {

    const user = $("username").value.trim();
    const pass = $("pass").value.trim();

    if (!user || !pass) {
        $("mensaje").textContent = "Complete todos los campos.";
        return;
    }

    let payload = {username:user, password:pass}

    try {

        const response = await apiCall("login",payload);

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