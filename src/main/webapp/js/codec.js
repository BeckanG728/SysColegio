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




//
//function addRowTable(c1,c2){
//    if(c1 === '' || c2 === ''){
//        alert("Uno de los campos esta vacio");
//        return;
//    }
//    
//    let table = $("table");
//    let newRow = table.insertRow();
//    
//    let cel1 = newRow.insertCell(0);
//    let cel2 = newRow.insertCell(1);
//    
//    cel1.innerHTML = c1;
//    cel2.innerHTML = c2;
//}

