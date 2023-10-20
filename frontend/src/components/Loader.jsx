import {Header} from "./Header.jsx";
import {Page} from "./Page.jsx";
import {App} from "./App.jsx";

export const Loader = ({}) => {
    return (
        <App>
            <Header/>
            <Page>
                <div
                    className="inline-block h-64 w-64 animate-spin rounded-full border-4 border-solid border-current border-r-transparent align-[-0.125em] motion-reduce:animate-[spin_1.5s_linear_infinite]"
                    role="status">
                    <span
                        className="!absolute !-m-px !h-px !w-px !overflow-hidden !whitespace-nowrap !border-0 !p-0 ![clip:rect(0,0,0,0)]"
                    >
                        Loading...
                    </span>
                </div>
            </Page>
        </App>
    )
}