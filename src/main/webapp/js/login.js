const $ = (id) => document.getElementById(id);
 
$("login").addEventListener("click", async () => {
 
    const user = $("username").value.trim();
    const pass = $("pass").value.trim();
 
    if (!user || !pass) {
        $("mensaje").textContent = "Complete todos los campos.";
        return;
    }
 
    // Cifrado: César -> Base64  (igual que el servidor hace al revés)
    const userCifrado = btoa(cifrar(user));
    const passCifrado = btoa(cifrar(pass));
 
    try {
        const response = await fetch("login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                username: userCifrado,
                password: passCifrado
            })
        });
 
        // El servidor siempre responde texto cifrado con César
        const textoCifrado = await response.text();
        const textoPlano   = descifrar(textoCifrado);
        console.log(textoPlano);
        const resultado    = JSON.parse(textoPlano);
 
        if (resultado.resultado === "ok") {
            // Redirigir al dashboard
            window.location.href = "app/dashboard.html";
        } else {
            $("mensaje").textContent = resultado.mensaje || "Credenciales incorrectas.";
        }
 
    } catch (error) {
        console.error(error);
        $("mensaje").textContent = "Error de conexión.";
    }
});