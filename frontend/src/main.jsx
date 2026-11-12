import React,{useEffect,useState} from "react";
import {createRoot} from "react-dom/client";
import "./style.css";

const API=import.meta.env.VITE_API_URL||"http://localhost:8080/api";
const credentials=btoa(`${localStorage.getItem("user")||"holder"}:${localStorage.getItem("password")||"holder-password"}`);
async function request(path,options={}){const r=await fetch(API+path,{...options,headers:{"Content-Type":"application/json","Authorization":`Basic ${credentials}`,...(options.headers||{})}});if(!r.ok)throw new Error((await r.json().catch(()=>({error:r.statusText}))).error||"Request failed");return r.status===204?null:r.json();}
function App(){const [accounts,setAccounts]=useState([]),[message,setMessage]=useState(""),[amount,setAmount]=useState(""),[selected,setSelected]=useState("");
 const load=()=>request("/accounts").then(setAccounts).catch(e=>setMessage(e.message)); useEffect(load,[]);
 async function create(){try{await request("/accounts",{method:"POST"});setMessage("Account created");load();}catch(e){setMessage(e.message)}}
 async function deposit(){try{if(!selected)throw Error("Select an account");await request(`/accounts/${selected}/deposit`,{method:"POST",headers:{"Idempotency-Key":crypto.randomUUID()},body:JSON.stringify({amount:Number(amount)})});setAmount("");setMessage("Deposit recorded");load();}catch(e){setMessage(e.message)}}
 return <main><header><span>LedgerLine</span><small>Provable money movement</small></header><section className="hero"><h1>Your accounts</h1><p>Balances are updated with a transaction-safe double-entry ledger.</p><button onClick={create}>Create account</button></section><section className="grid">{accounts.map(a=><article key={a.id}><small>ACCOUNT</small><h2>₹ {Number(a.balance).toFixed(2)}</h2><code>{a.id}</code></article>)}{!accounts.length&&<p>No accounts yet. Create one to begin.</p>}</section><section className="panel"><h2>Deposit funds</h2><select value={selected} onChange={e=>setSelected(e.target.value)}><option value="">Choose account</option>{accounts.map(a=><option key={a.id} value={a.id}>{a.id}</option>)}</select><input type="number" min="0.01" step="0.01" placeholder="Amount" value={amount} onChange={e=>setAmount(e.target.value)}/><button onClick={deposit}>Deposit</button>{message&&<p className="message">{message}</p>}</section></main>}
createRoot(document.getElementById("root")).render(<App/>);
