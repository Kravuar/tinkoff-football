import {Page} from "../components/Page.jsx";
import {Header} from "../components/Header.jsx";
import {App} from "../components/App.jsx";


function Root() {
    return (
        <App>
            <Header/>
            <Page>
                <h1 className="text-3xl font-bold underline">
                    Hello world!
                </h1>
                {/*<div className={'bg-green-300 w-64 h-64'}/>*/}
                {/*<div className={'bg-green-300 w-64 h-64'}/>*/}
                {/*<div className={'bg-green-300 w-64 h-64'}/>*/}
                {/*<div className={'bg-green-300 w-64 h-64'}/>*/}
            </Page>
        </App>
    )
}

export default Root
