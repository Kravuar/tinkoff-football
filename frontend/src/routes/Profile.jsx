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
import {useTeams} from "../queries.js";


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

const Team = ({team}) => {
    const user = useUser(state => state.user)
    const secondPlayer = team.secondPlayer.id === user.id ? team.captain : team.secondPlayer

    const can_accept = team.secondPlayerStatus === 'INVITED' && team.secondPlayer.id === user.id
    const can_cancel = team.secondPlayerStatus === 'INVITED' && team.captain.id === user.id
    const can_reject = team.secondPlayerStatus === 'INVITED' && team.secondPlayer.id === user.id
    console.log(can_accept, can_cancel, can_reject, user.id, team.secondPlayer.id)

    const client = useQueryClient()
    const joinMutation = useMutation((id) => api.put(`/teams/${id}/join/`), {
        onSettled: () => {
            client.invalidateQueries(['teams'])
        }
    })
    const cancelMutation = useMutation((id) => api.delete(`/teams/${id}/declineInvite`), {
        onSettled: () => {
            client.invalidateQueries(['teams'])
        }
    })
    return (
        <Tr className="bg-white border-b odd:bg-white even:bg-gray-50"
            key={team.id}>
            <ThTd>
                {team.name}
            </ThTd>
            <Td>
                {secondPlayer.username}
            </Td>
            <Td>
                {secondPlayer.id}
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
                        <div className={'flex gap-2'}>
                            {
                                can_cancel ? (
                                    <TeamModal onSubmit={() => cancelMutation.mutate(team.id)}
                                               title={'Отменить приглашение'}
                                               subtitle={'Вы уверены, что хотите отменить приглашение?'}>
                                        <XMarkIcon title={'Отменить'}
                                                   className="h-6 w-6 stroke-2 text-red-500"/>
                                    </TeamModal>
                                ) : null
                            }
                            {
                                can_accept ? (
                                    <button onClick={() => joinMutation.mutate(team.id)}>
                                        <CheckIcon title={'Присоединиться'} className="h-6 w-6 text-green-500"/>
                                    </button>
                                ) : null
                            }
                            {
                                can_reject ? (
                                    <TeamModal onSubmit={() => cancelMutation.mutate(team.id)}
                                               title={'Отказаться от присоединения к команде'}
                                               subtitle={'Вы уверены, что хотите оказаться?'}>
                                        <XMarkIcon title={'Отказаться'}
                                                   className="h-6 w-6 stroke-2 text-red-500"/>
                                    </TeamModal>
                                ) : null
                            }
                        </div>
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
}

const Teams = () => {
    const client = useQueryClient()
    const {register, handleSubmit} = useForm()
    const {data: teams, isLoading, isError} = useTeams()
    console.log(teams)
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
            className={'w-full mt-14 p-3 md:p-8 border-t-gray-600 border-t-[5px] bg-white mx-auto rounded-3xl shadow-2xl'}>
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
                                        teams.map(team => <Team key={team.id} team={team}/>)
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

const Tournaments = () => {
    const {data, isLoading, isError} = useQuery(['tournaments-my'],
        async () => (await api.get('/tournaments/myHistory')).data)

    return (
        <div
            className={'w-full mt-14 p-3 md:p-8 border-t-gray-600 border-t-[5px] bg-white mx-auto rounded-3xl shadow-2xl'}>
            <div className={'flex flex-col gap-4'}>
                <h1 className={'text-2xl font-semibold'}>
                    Ваши турниры
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
                        <div className="overflow-x-auto shadow-lg sm:rounded-2xl">
                            <table className="w-full text-sm text-left text-gray-500">
                                <thead
                                    className="text-xs text-gray-700 uppercase bg-gray-50">
                                <Tr>
                                    <Th>Название турнира</Th>
                                    <Th>Организатор</Th>
                                    <Th>Статус</Th>
                                </Tr>
                                </thead>
                                <tbody>
                                {
                                    data?.tournaments.map(tournament => {
                                        return (
                                            <Tr key={tournament.id}>
                                                <ThTd>
                                                    <Link to={`/tournaments/${tournament.id}`}>
                                                        {tournament.title}
                                                    </Link>
                                                </ThTd>
                                                <Td>
                                                    {tournament.owner.username}
                                                </Td>
                                                <Td>
                                                    {
                                                        {
                                                            PENDING: <CalendarDaysIcon title={'Открыт'}
                                                                className={"h-6 w-6 text-gray-500 stroke-2"}/>,
                                                            ACTIVE: <PlayIcon title={'Продолжается'}
                                                                className={'h-6 w-6 text-green-500 stroke-2'}/>,
                                                            FINISHED: <TrophyIcon title={'Завершен'}
                                                                className={'h-6 w-6 text-primary stroke-2'}/>,
                                                            CANCELED: <XMarkIcon title={'Отменен'}
                                                                className={'h-6 w-6 text-primary stroke-2'}/>,
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
                <div className={'pt-[100px] md:pt-[200px] flex flex-col items-center w-full'}>

                    <div className={'p-3'}>
                        <h1 className={'text-[44px] font-bold'}>
                            Здравствуйте, {user.login}#{user.id}
                        </h1>
                    </div>

                    <Teams/>
                    <Tournaments/>
                </div>
            </Page>
        </App>
    )
}