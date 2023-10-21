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
import {ReactQueryDevtools} from "@tanstack/react-query-devtools";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {Tournaments} from "./routes/Tournaments.jsx";
import {TournamentGrid} from "./routes/TournamentGrid.jsx";
import {Tournament} from "./routes/Tournament.jsx";


const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: 1000 * 60 * 2,
            refetchOnWindowFocus: false,
            refetchOnMount: true,
            keepPreviousData: true,
        }
    }
})

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
    },
    {
        path: "/tournaments",
        element: <Tournaments/>,
        errorElement: <ErrorPage/>
    },
    {
        path: "/tournaments/:id",
        element: <Tournament/>,
        errorElement: <ErrorPage/>
    },
    {
        path: "/tournaments/:id/grid",
        element: <TournamentGrid/>,
        errorElement: <ErrorPage/>
    }
]);

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router}/>
            <ReactQueryDevtools initialIsOpen={false}/>
        </QueryClientProvider>
    </React.StrictMode>,
)
