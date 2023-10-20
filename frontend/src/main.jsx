import React from 'react'
import ReactDOM from 'react-dom/client'
import Root from './routes/Root.jsx'
import './index.css'
import {
    createBrowserRouter,
    RouterProvider,
} from "react-router-dom";
import {ErrorPage} from "./routes/ErrorPage.jsx";
import {Registration} from "./routes/Registration.jsx";
import {Authorization} from "./routes/Authorization.jsx";
import {Profile} from "./routes/Profile.jsx";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Root/>,
        errorElement: <ErrorPage/>,
    },
    {
        path: "/registration",
        element: <Registration/>,
        errorElement: <ErrorPage/>
    },
    {
        path: "/authorization",
        element: <Authorization/>,
        errorElement: <ErrorPage/>
    },
    {
        path: "/profile",
        element: <Profile/>,
        errorElement: <ErrorPage/>
    }
]);

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <RouterProvider router={router}/>
    </React.StrictMode>,
)
