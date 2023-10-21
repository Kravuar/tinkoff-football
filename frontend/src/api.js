import axios from "axios";


export const baseURL = "http://localhost:3000";

// *глобальный* объект для совершения запросов
export const api = axios.create({
    withCredentials: true,
    baseURL: baseURL,
});