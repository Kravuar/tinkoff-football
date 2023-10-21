import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {CheckIcon, ChevronRightIcon, XMarkIcon} from "@heroicons/react/24/outline";
import {Input} from "../components/Input.jsx";
import {PrimaryButton, WhiteButton} from "../components/Button.jsx";
import {useState} from "react";
import {ActionsContainer, Modal, Title} from "../components/modal/Modal.jsx";
import {useUser} from "../hooks/useUser.jsx";
import {useForm} from "react-hook-form";


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


const TeamModal = ({onSubmit, title, subtitle, children}) => {
    const [open, setOpen] = useState(false)
    const onClose = () => setOpen(false)
    const onSubmitInner = () => {
        onSubmit()
        onClose()
    }
    return (
        <button onClick={() => setOpen(true)}>
            {children}
            <Modal isOpen={open} onClose={onClose}>
                <Title>
                    {title}
                </Title>
                <div className="mt-2">
                    {subtitle}
                </div>

                <ActionsContainer>
                    <WhiteButton onClick={onClose}>
                        Отмена
                    </WhiteButton>
                    <PrimaryButton onClick={onSubmitInner}>
                        Да
                    </PrimaryButton>
                </ActionsContainer>
            </Modal>
        </button>
    )
}

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
                        Статус
                    </th>
                    <th scope="col" className="px-6 py-3">
                    </th>
                </tr>
                </thead>
                <tbody>
                {
                    teams.map(team => {
                        const accept = () => {
                            console.log('accept invitation')
                        }
                        const leave = () => {
                            console.log('leave team')
                        }
                        const cancel = () => {
                            console.log('cancel invitation')
                        }
                        return (
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
                                            ok: 'Вы в команде',
                                            me_invited: 'Вы пригласили',
                                            i_invited: 'Вас пригласили',
                                        } [team.status]
                                    }
                                </td>
                                <td className={'px-6 py-4'}>
                                    {
                                        {
                                            ok: <TeamModal onSubmit={leave} title={'Выход из команды'}
                                                           subtitle={'Вы уверены, что хотите покинуть команду?'}>
                                                <XMarkIcon className="h-6 w-6 stroke-2 text-red-500"/>
                                            </TeamModal>,
                                            me_invited: <TeamModal onSubmit={cancel} title={'Отменить приглашение'}
                                                                   subtitle={'Вы уверены, что хотите отменить приглашение?'}>
                                                <XMarkIcon className="h-6 w-6 stroke-2 text-red-500"/>
                                            </TeamModal>,
                                            i_invited: <button onClick={accept}>
                                                <CheckIcon className="h-6 w-6 text-green-500"/>
                                            </button>,
                                        } [team.status]
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

export const Profile = () => {
    const user = useUser(state => state.user)
    const {register, handleSubmit, watch, formState: {errors}} = useForm();
    const onSubmit = (data) => {
        console.log(data)
    }
    return (
        <App>
            <Header/>
            <Page>
                <div className={'pt-[170px] pb-[300px] flex flex-col items-center'}>

                    <div className={'p-3'}>
                        <h1 className={'text-[44px] font-bold'}>
                            Здравствуйте, {user.login}#{user.id}
                        </h1>
                    </div>

                    <div
                        className={'mt-14 p-8 border-t-gray-600 border-t-[5px] bg-white mx-auto rounded-3xl shadow-2xl'}>
                        <div className={'flex flex-col gap-4'}>
                            <form className={'rounded-2xl flex gap-1'} onSubmit={handleSubmit(onSubmit)}>
                                <Input className={'w-full shadow-lg'} placeholder={'Название команды'} {...register('name', {required: true})}/>
                                <Input className={'w-full shadow-lg'} placeholder={'ID игрока'} {...register('user_id', {required: true})}/>
                                <PrimaryButton className={'shadow-lg'}>
                                    <ChevronRightIcon className="h-6 w-6 text-gray-500 stroke-[3]"/>
                                </PrimaryButton>
                            </form>
                            <Table/>
                        </div>
                    </div>


                </div>
            </Page>
        </App>
    )
}