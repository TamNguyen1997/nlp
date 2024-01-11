async function getPageTitle() {
  let text = document.body.innerText
  let defaultResult = await scanByDefault(text)
  let regexResult = await scanByRegex(text)
  let dictResult = await scanByDict(text)
  let orgResult = await scanOrg(text)

  alert("Default: " + await defaultResult.text())
  alert("Regex: " + await regexResult.text())
  alert("Dict: " + await dictResult.text())
  alert("Org: " + await orgResult.text())
}

function scanByDefault(text) { 
  console.log("start sending for default: "  + text) 
  return fetch("http://localhost:8083/default/name", {
  method: "POST",
  body: JSON.stringify({
    text: text
  }),
  headers: {
    "Content-type": "application/json; charset=UTF-8"
  }
})}

function scanByRegex(text) { // Phrases with 2 words each starts with a capital letter
  console.log("start sending for regex: "  + text) 
  return fetch("http://localhost:8083/regex/name", {
  method: "POST",
  body: JSON.stringify({
    text: text
  }),
  headers: {
    "Content-type": "application/json; charset=UTF-8"
  }
})}

function scanByDict(text) { // Dictionary contains BWM, Toyota, Honda and RollRoyce
  console.log("start sending for dict: "  + text)
  return fetch("http://localhost:8083/dictionary/name", {
  method: "POST",
  body: JSON.stringify({
    text: text
  }),
  headers: {
    "Content-type": "application/json; charset=UTF-8"
  }
})}

function scanOrg(text) { // Learning material include Netcompany and Cadoozle
  console.log("start sending for org: "  + text)
  return fetch("http://localhost:8083/organisation/name", {
  method: "POST",
  body: JSON.stringify({
    text: text
  }),
  headers: {
    "Content-type": "application/json; charset=UTF-8"
  }
})}

getPageTitle();