const $ = (id) => document.getElementById(id);

function cifrar(textPlain){
    let textCipher = "";
    for(let i = 0; i < textPlain.length; i++){
        let code = textPlain.charCodeAt(i);
        code += 3;
        textCipher += String.fromCharCode(code);
    }
    return textCipher;
}

function descifrar(textCipher){
    let textPlain = "";
    for(let i = 0; i < textCipher.length; i++){
        let code = textCipher.charCodeAt(i);
        code -= 3;  // Restar 3 para descifrar (no sumar)
        textPlain += String.fromCharCode(code);
    }
    return textPlain;
}


$("btnVerify").addEventListener("click", async () => {
    const textPlain  = $("dni").value;
    const textCipher = cifrar(textPlain);
    
    console.log(descifrar(textCipher));

    const params = new URLSearchParams();
    params.append("dni", textCipher);

    try {
        const response = await fetch("alumno", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: params
        });

        const cifrado = await response.text();
        const descifrado = descifrar(cifrado);
        console.log(descifrado);
        console.log(cifrado);

        addRowTable(descifrado,cifrado);

    } catch (error) {
        console.log(error);
    }
});


function addRowTable(c1,c2){
    if(c1 === '' || c2 === ''){
        alert("Uno de los campos esta vacio");
        return;
    }
    
    let table = $("table");
    let newRow = table.insertRow();
    
    let cel1 = newRow.insertCell(0);
    let cel2 = newRow.insertCell(1);
    
    cel1.innerHTML = c1;
    cel2.innerHTML = c2;
}
