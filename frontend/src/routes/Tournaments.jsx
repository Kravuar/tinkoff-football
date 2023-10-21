import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {PlayIcon} from "@heroicons/react/24/outline/index.js";
import {ArrowPathIcon, TrophyIcon} from "@heroicons/react/24/outline";
import {PrimaryButton} from "../components/Button.jsx";
import {Link} from "react-router-dom";

const tournaments = [
    {name: 'Big tournament', owner: "danil#4122", status: "active"},
    {name: 'Small tournament', owner: "andrew#1122", status: "finished"},
    {name: 'Medium tournament', owner: "roman#1122", status: "opened"},
    {name: 'Big tournament', owner: "danil#4122", status: "active"},
    {name: 'Small tournament', owner: "andrew#1122", status: "finished"},
    {name: 'Medium tournament', owner: "roman#1122", status: "opened"},
    {name: 'Big tournament', owner: "danil#4122", status: "active"},
    {name: 'Small tournament', owner: "andrew#1122", status: "finished"},
    {name: 'Medium tournament', owner: "roman#1122", status: "opened"},
    {name: 'Big tournament', owner: "danil#4122", status: "active"},
    {name: 'Small tournament', owner: "andrew#1122", status: "finished"},
    {name: 'Medium tournament', owner: "roman#1122", status: "opened"},
].map((obj, id) => {
    obj.id = id
    return obj
})

const Table = () => {
    return (
        <div className="overflow-x-auto shadow-lg sm:rounded-2xl">
            <table className="w-full text-sm text-left text-gray-500">
                <thead
                    className="text-xs text-gray-700 uppercase bg-gray-50">
                <tr>
                    <th scope="col" className="px-6 py-3">
                        Название турнира
                    </th>
                    <th scope="col" className="px-6 py-3">
                        Организатор
                    </th>
                    <th scope="col" className="px-6 py-3">
                        Статус
                    </th>
                </tr>
                </thead>
                <tbody>
                {
                    tournaments.map(tournament => {
                        return (
                            <tr className="bg-white border-b odd:bg-white even:bg-gray-50" key={tournament.id}>
                                <th scope="row"
                                    className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
                                    <Link to={`/tournaments/${tournament.id}`}>
                                        {tournament.name}
                                    </Link>
                                </th>
                                <td className="px-6 py-4">
                                    {tournament.owner}
                                </td>
                                <td className="px-6 py-4">
                                    {
                                        {
                                            opened: '',
                                            active: <PlayIcon className={'h-6 w-6 text-green-500 stroke-2'}/>,
                                            finished: <TrophyIcon className={'h-6 w-6 text-primary stroke-2'}/>
                                        }[tournament.status]
                                    }
                                </td>
                            </tr>
                        )
                    })
                }
                </tbody>
            </table>
        </div>
    )
}

export const Tournaments = () => {
    const onUpdate = () => {
        console.log('update tournaments requested')
    }
    return (
        <App>
            <Header/>
            <Page>
                <div className={'pt-[80px] pb-[150px] flex flex-col items-center'}>

                    <div className={'p-3'}>
                        <h1 className={'text-[44px] font-bold'}>
                            Все турниры
                        </h1>
                    </div>
                    <PrimaryButton className={'mt-8'}>
                        Создать турнир
                    </PrimaryButton>

                    <div
                        className={'mt-14 w-full p-8 border-t-gray-600 border-t-[5px] bg-white mx-auto rounded-3xl shadow-2xl'}>
                        <div className={'flex justify-end'}>
                            <PrimaryButton onClick={onUpdate}>
                                <ArrowPathIcon className="animate-spin h-6 w-6 text-gray-500 stroke-2"/>
                            </PrimaryButton>
                        </div>
                        <div className={'mt-4 flex flex-col gap-4'}>
                            <Table/>
                        </div>
                    </div>
                </div>
            </Page>
        </App>
    )
}