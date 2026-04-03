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