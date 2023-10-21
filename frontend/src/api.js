import axios from "axios";


export const baseURL = "http://localhost:8080";

// *глобальный* объект для совершения запросов
export const api = axios.create({
    withCredentials: true,
    baseURL: baseURL,
    headers: {
        "Access-Control-Allow-Origin": "*"
    }
});