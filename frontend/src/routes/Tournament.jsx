import {useParams} from "react-router-dom";
import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";

const tournament = {
    name: "Большой турнир",
    status: "opened"
}

export const Tournament = () => {
    const {id} = useParams();

    return (
        <App>
            <Header/>
            <Page>
                <div className={'mt-[100px] flex flex-col items-center w-full min-h-[85vh]'}>
                    <h1 className={'font-semibold text-3xl'}>
                        Турнир "{tournament.name}#{id}"
                    </h1>
                    <div className={'mt-4'}>
                        {
                            {
                                opened: "Турнир открыт",
                                active: "Турнир продолжается",
                                finished: "Турнир завершен"
                            }[tournament.status]
                        }
                    </div>
                </div>
            </Page>
        </App>
    )
}