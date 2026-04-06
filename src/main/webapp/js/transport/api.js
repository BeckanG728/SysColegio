import { decrypt, encrypt } from "../crypto/cesar-bytes.js";

const encoder = new TextEncoder();
const decoder = new TextDecoder();

export async function apiCall(endpoint, payload) {

    // 1. Serialización → bytes
    const plainBytes = encoder.encode(JSON.stringify(payload));

    // 2. Cifrado
    const encryptedBytes = encrypt(plainBytes);

    console.log("ENVIADO (bytes):", encryptedBytes);

    // 3. Envío binario
    const res = await fetch(endpoint, {
        method: "POST",
        headers: {
            "Content-Type": "application/octet-stream"
        },
        body: encryptedBytes
    });

    // 4. Recepción binaria
    const buffer = await res.arrayBuffer();
    const receivedBytes = new Uint8Array(buffer);

    console.log("RECIBIDO (bytes):", receivedBytes);

    // 5. Descifrado
    const decryptedBytes = decrypt(receivedBytes);

    // 6. Bytes → string
    const json = decoder.decode(decryptedBytes);

    console.log("RECIBIDO (plano):", json);

    // 7. Deserialización
    return JSON.parse(json);
}

export async function apiGet(endpoint, params = {}) {

    // 1. Query string
    const query = new URLSearchParams(params).toString();
    const url = query ? `${endpoint}?${query}` : endpoint;

    // 2. Request GET
    const res = await fetch(url, {
        method: "GET",
        headers: {
            "Accept": "application/octet-stream"
        }
    });

    // 3. Validación
    if (!res.ok) {
        throw new Error(`HTTP error: ${res.status}`);
    }

    // 4. Recepción binaria
    const buffer = await res.arrayBuffer();
    const receivedBytes = new Uint8Array(buffer);

    console.log("RECIBIDO (bytes):", receivedBytes);

    // 5. Descifrado
    const decryptedBytes = decrypt(receivedBytes);

    // 6. Bytes → string
    const json = decoder.decode(decryptedBytes);

    console.log("RECIBIDO (plano):", json);

    // 7. Parseo
    return JSON.parse(json);
}

/*
async function apiCall(endpoint, payload) {
    const body = cifrar(JSON.stringify(payload));
    
    const res = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'text/plain;charset=UTF-8' },
        body: body
    });
    const raw = await res.text();
    console.log(raw);
    const decrypted = descifrar(raw);
    console.log(decrypted);
    return JSON.parse(decrypted);
}
*/
