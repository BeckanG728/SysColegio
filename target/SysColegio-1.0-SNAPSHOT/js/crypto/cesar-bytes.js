const SHIFT = 3;

export function encrypt(data) {
    const result = new Uint8Array(data.length);

    for (let i = 0; i < data.length; i++) {
        result[i] = (data[i] + SHIFT) % 256;
    }

    return result;
}

export function decrypt(data) {
    const result = new Uint8Array(data.length);

    for (let i = 0; i < data.length; i++) {
        result[i] = (data[i] - SHIFT + 256) % 256;
    }

    return result;
}

/*
const SHIFT = 3;
const MIN = 32;
const MAX = 126;
const RANGO = MAX - MIN + 1; // 95

function cifrar(textPlain) {
    let textCipher = "";
    for (let i = 0; i < textPlain.length; i++) {
        const code = textPlain.charCodeAt(i);
        if (code >= MIN && code <= MAX) {
            textCipher += String.fromCharCode(MIN + (code - MIN + SHIFT) % RANGO);
        } else {
            textCipher += textPlain[i];
        }
    }
    return textCipher;
}

function descifrar(textCipher) {
    let textPlain = "";
    for (let i = 0; i < textCipher.length; i++) {
        const code = textCipher.charCodeAt(i);
        if (code >= MIN && code <= MAX) {
            textPlain += String.fromCharCode(MIN + (code - MIN - SHIFT + RANGO) % RANGO);
        } else {
            textPlain += textCipher[i];
        }
    }
    return textPlain;
}
*/