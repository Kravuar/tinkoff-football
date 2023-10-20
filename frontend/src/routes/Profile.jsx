import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {CheckIcon, ChevronRightIcon, XMarkIcon} from "@heroicons/react/24/outline";
import {Input} from "../components/Input.jsx";
import {Button} from "../components/Button.jsx";


/* состояния
    null
    ок
    я пригласил, еще не приняли
    меня пригласили, еще не принял
*/
const teams = [
    {
        id: 1,
        name: "Abobus Team",
        player_id: 1,
        player_name: "Aboba",
        status: "i_invited",
    },
    {
        id: 2,
        name: "Cringe Team",
        player_id: 2,
        player_name: "Cringe",
        status: "me_invited",
    },
    {
        id: 3,
        name: "Unknown Team",
        player_id: 3,
        player_name: "Player Unknown",
        status: "ok"
    },
    {
        id: 4,
        name: "Cringe Team",
        player_id: 2,
        player_name: "Cringe",
        status: "me_invited"
    },
    {
        id: 5,
        name: "Unknown Team",
        player_id: 3,
        player_name: "Player Unknown",
        status: "i_invited"
    }
]


const Table = () => {
    return (
        <div className="overflow-x-auto shadow-lg sm:rounded-2xl">
            <table className="w-full text-sm text-left text-gray-500">
                <thead
                    className="text-xs text-gray-700 uppercase bg-gray-50">
                <tr>
                    <th scope="col" className="px-6 py-3">
                        Название команды
                    </th>
                    <th scope="col" className="px-6 py-3">
                        Имя игрока
                    </th>
                    <th scope="col" className="px-6 py-3">
                        ID игрока
                    </th>
                    <th scope="col" className="px-6 py-3">

                    </th>
                </tr>
                </thead>
                <tbody>
                {
                    teams.map(team => (
                        <tr className="bg-white border-b odd:bg-white even:bg-gray-50" key={team.id}>
                            <th scope="row"
                                className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
                                {team.name}
                            </th>
                            <td className="px-6 py-4">
                                {team.player_name}
                            </td>
                            <td className="px-6 py-4">
                                {team.player_id}
                            </td>
                            <td className="px-6 py-4">
                                {
                                    {
                                        ok: <XMarkIcon class="h-6 w-6 stroke-2 text-red-500" />,
                                        me_invited: <XMarkIcon class="h-6 w-6 stroke-2 text-red-500" />,
                                        i_invited: <CheckIcon class="h-6 w-6 text-green-500" />,
                                    }
                                        [team.status]
                                }
                            </td>
                        </tr>
                    ))
                }
                </tbody>
            </table>
        </div>
    )
}

export const Profile = () => {
    return (
        <App>
            <Header/>
            <Page>
               <div className={'pt-[250px] pb-[300px]'}>

                   
                   <div className={'p-8 bg-white mx-auto max-w-[500px] rounded-3xl'}>
                       <div className={'flex flex-col gap-4'}>
                           <div className={'rounded-2xl flex gap-1'}>
                               <Input className={'w-full shadow-lg'} placeholder={'Название команды'} />
                               <Button className={'shadow-lg'}>
                                   <ChevronRightIcon class="h-6 w-6 text-gray-500 stroke-[3]"/>
                               </Button>
                           </div>
                           <Table/>
                       </div>
                   </div>


               </div>
            </Page>
        </App>
    )
}