import {App} from "../components/App.jsx";
import {useRouteError} from "react-router-dom";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";

export const ErrorPage = ({}) => {
    const error = useRouteError();
    console.error(error);
    return (
        <App>
            <Header/>
            <Page>
                <h1>Oops!</h1>
                <p>Sorry, an unexpected error has occurred.</p>
                <p>
                    <i>{error.statusText || error.message}</i>
                </p>
            </Page>
        </App>
    )
}