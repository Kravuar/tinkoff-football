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
import {Td, Th, ThTd, Tr} from "../components/table/Table.jsx";
import {Link} from "react-router-dom";
import {CalendarDaysIcon, PlayIcon, TrophyIcon} from "@heroicons/react/24/outline/index.js";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {api} from "../api.js";
import {Spinner} from "../components/Spinner.jsx";


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

const Teams = () => {
    const client = useQueryClient()
    const {register, handleSubmit} = useForm()
    const {data: teams, isLoading, isError} = useQuery(
        ['teams'],
        () => api.get('/teams/my'), {
            initialData: []
        }
    )
    const addTeamMutation = useMutation((data) => api.post('/teams/create', {
        name: data.name,
        secondPlayerId: data.user_id,
    }), {
        onSettled: () => {
            client.invalidateQueries(['teams'])
        }
    })
    const onSubmit = (data) => {
        console.log(data)
        addTeamMutation.mutate(data)
    }

    return (
        <div
            className={'w-full mt-14 p-8 border-t-gray-600 border-t-[5px] bg-white mx-auto rounded-3xl shadow-2xl'}>
            <div className={'flex flex-col gap-4'}>
                <h1 className={'text-2xl font-semibold'}>
                    Ваши команды
                </h1>
                {
                    isError || isLoading ? (
                        isLoading ? (
                            <div className={'w-full flex justify-center items-center'}>
                                <Spinner/>
                            </div>
                        ) : (
                            <div>
                                Ошибка загрузки ваших команд
                            </div>
                        )
                    ) : (
                        <>
                            <form className={'rounded-2xl flex gap-1'} onSubmit={handleSubmit(onSubmit)}>
                                <Input className={'w-full shadow-lg'}
                                       placeholder={'Название команды'} {...register('name', {required: true})}/>
                                <Input className={'w-full shadow-lg'}
                                       placeholder={'ID игрока'} {...register('user_id', {required: true})}/>
                                <PrimaryButton className={'shadow-lg'}>
                                    <ChevronRightIcon className="h-6 w-6 text-gray-500 stroke-[3]"/>
                                </PrimaryButton>
                            </form>
                            <div className="mt-4 overflow-x-auto shadow-lg sm:rounded-2xl">
                                <table className="w-full text-sm text-left text-gray-500">
                                    <thead
                                        className="text-xs text-gray-700 uppercase bg-gray-50">
                                    <Tr>
                                        <Th>Название команды</Th>
                                        <Th>Имя игрока</Th>
                                        <Th>ID игрока</Th>
                                        <Th>Статус</Th>
                                        <Th></Th>
                                    </Tr>
                                    </thead>
                                    <tbody>
                                    {
                                        teams.map(team => {
                                            const joinMutation = useMutation((id) => api.put(`/teams/${id}/join`), {
                                                onSettled: () => {
                                                    client.invalidateQueries(['teams'])
                                                }
                                            })
                                            const cancelMutation = useMutation((id) => api.delete(`/teams/${id}/declineInvite`), {
                                                onSettled: () => {
                                                    client.invalidateQueries(['teams'])
                                                }
                                            })
                                            const cancel = () => {
                                                console.log('cancel invitation')
                                            }
                                            return (
                                                <Tr className="bg-white border-b odd:bg-white even:bg-gray-50"
                                                    key={team.id}>
                                                    <ThTd>
                                                        {team.name}
                                                    </ThTd>
                                                    <Td>
                                                        {team.secondPlayer.username}
                                                    </Td>
                                                    <Td>
                                                        {team.secondPlayer.id}
                                                    </Td>
                                                    <Td>
                                                        {
                                                            {
                                                                INVITED: 'Вы пригласили',
                                                                JOINED: 'Вы в команде',
                                                            } [team.secondPlayerStatus]
                                                        }
                                                    </Td>
                                                    <Td>
                                                        {
                                                            cancelMutation.isLoading || joinMutation.isLoading ? (
                                                                <Spinner/>
                                                            ) : (
                                                                <>
                                                                    <TeamModal onSubmit={() => cancelMutation.mutate()}
                                                                               title={'Отменить приглашение'}
                                                                               subtitle={'Вы уверены, что хотите отменить приглашение?'}>
                                                                        <XMarkIcon
                                                                            className="h-6 w-6 stroke-2 text-red-500"/>
                                                                    </TeamModal>
                                                                    <button onClick={() => joinMutation.mutate()}>
                                                                        <CheckIcon className="h-6 w-6 text-green-500"/>
                                                                    </button>
                                                                </>
                                                            )
                                                        }
                                                        {/*<TeamModal onSubmit={leave}*/}
                                                        {/*           title={'Выход из команды'}*/}
                                                        {/*           subtitle={'Вы уверены, что хотите покинуть команду?'}>*/}
                                                        {/*    <XMarkIcon*/}
                                                        {/*        className="h-6 w-6 stroke-2 text-red-500"/>*/}
                                                        {/*</TeamModal>*/}
                                                    </Td>
                                                </Tr>
                                            )
                                        })
                                    }
                                    </tbody>
                                </table>
                            </div>
                        </>
                    )
                }
            </div>
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
                <div className={'pt-[170px] pb-[300px] flex flex-col items-center w-full'}>

                    <div className={'p-3'}>
                        <h1 className={'text-[44px] font-bold'}>
                            Здравствуйте, {user.login}#{user.id}
                        </h1>
                    </div>

                    <Teams/>

                    <div
                        className={'w-full mt-14 p-8 border-t-gray-600 border-t-[5px] bg-white mx-auto rounded-3xl shadow-2xl'}>
                        <div className={'flex flex-col gap-4'}>
                            <h1 className={'text-2xl font-semibold'}>
                                Ваши турниры
                            </h1>
                            <div className="overflow-x-auto shadow-lg sm:rounded-2xl">
                                <table className="w-full text-sm text-left text-gray-500">
                                    <thead
                                        className="text-xs text-gray-700 uppercase bg-gray-50">
                                    <Tr>
                                        <Th>Название турнира</Th>
                                        <Th>Организатор</Th>
                                        <Th>Статус</Th>
                                        <Th></Th>
                                    </Tr>
                                    </thead>
                                    <tbody>
                                    {
                                        tournaments.map(tournament => {
                                            return (
                                                <Tr key={tournament.id}>
                                                    <ThTd>
                                                        <Link to={`/tournaments/${tournament.id}`}>
                                                            {tournament.name}
                                                        </Link>
                                                    </ThTd>
                                                    <Td>
                                                        {tournament.owner}
                                                    </Td>
                                                    <Td>
                                                        {
                                                            {
                                                                opened: <CalendarDaysIcon
                                                                    className={"h-6 w-6 text-gray-500 stroke-2"}/>,
                                                                active: <PlayIcon
                                                                    className={'h-6 w-6 text-green-500 stroke-2'}/>,
                                                                finished: <TrophyIcon
                                                                    className={'h-6 w-6 text-primary stroke-2'}/>
                                                            }[tournament.status]
                                                        }
                                                    </Td>
                                                </Tr>
                                            )
                                        })
                                    }
                                    </tbody>
                                </table>
                            </div>

                        </div>
                    </div>
                </div>
            </Page>
        </App>
    )
}