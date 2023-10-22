import {Link, useParams} from "react-router-dom";
import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {PrimaryButton} from "../components/Button.jsx";
import {Controller, useForm} from "react-hook-form";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {api} from "../api.js";
import Select from "react-select";
import {useTeams} from "../queries.js";
import {Spinner} from "../components/Spinner.jsx";
import {Td, Th, ThTd, Tr} from "../components/table/Table.jsx";
import {CalendarDaysIcon, PlayIcon, TrophyIcon} from "@heroicons/react/24/outline/index.js";

const tournament = {
    name: "Большой турнир", status: "opened"
}

const TeamSelect = ({...props}) => {
    const {data: teams, isLoading} = useTeams()
    console.log(teams)
    const options = teams?.map(team => {
        return {
            id: team.id,
            value: team.id,
            label: team.name
        }
    })
    return (
        <Select
            options={options}
            isLoading={isLoading}
            placeholder={'Выберите команду'}
            styles={{
                control: (baseStyles) => ({
                    ...baseStyles,
                    padding: 9
                })
            }}
            {...props}
        />
    )
}

export const Tournament = () => {
    const {id} = useParams();
    const client = useQueryClient()
    const {control, handleSubmit} = useForm()
    const joinMutation = useMutation(['tournament', id], (team_id) => api.get(`/tournaments/${id}/join/${team_id}`), {
        onSettled: () => {
            client.invalidateQueries(['tournament', id])
        }
    })

    const {data: tournament, isLoading, isError} = useQuery(['tournament', id],
        async () => (await api.get(`/tournaments/${id}`)).data)

    const onJoin = (data) => {
        console.log(data)
        joinMutation.mutate(data.team.id)
    }

    return (<App>
        <Header/>
        <Page>
            <div className={'pt-[100px] flex flex-col items-center w-full'}>
                {
                    isLoading || isError ? (
                        isLoading ? (
                            <div className={'w-full flex justify-center items-center'}>
                                <Spinner/>
                            </div>
                        ) : (
                            <div>
                                Ошибка загрузки турнира
                            </div>
                        )
                    ) : (
                        <>
                            <h1 className={'font-bold text-[42px] text-center'}>
                                Турнир "{tournament.title}#{id}"
                            </h1>
                            <div className={'mt-4 flex justify-between gap-6 text-lg'}>
                                <div>
                                    {{
                                        PENDING: "Турнир открыт", ACTIVE: "Турнир продолжается",
                                        FINISHED: "Турнир завершен", CANCELED: "Турнир отменен"
                                    }[tournament.status]}
                                </div>
                                <div>
                                    Владелец: {tournament.owner.username}
                                </div>
                            </div>
                            <Link className={'mt-4'} to={`/tournaments/${id}/grid`}>
                                <PrimaryButton>
                                    Открыть сетку
                                </PrimaryButton>
                            </Link>

                            {/* список команд */}
                            <div className={'mt-16 w-full sm:w-[600px] p-3 md:p-8 bg-white rounded-2xl shadow-lg'}>
                                <div className={''}>
                                    Участников: {tournament.participants.length}/{tournament.maxParticipants}
                                </div>
                                <form className={'w-full flex gap-1 items-center'}
                                      onSubmit={handleSubmit(onJoin)}>
                                    {/*  выбор команды  */}
                                    <div className={'w-full'}>
                                        <Controller rules={{required: true}} control={control} name={'team'}
                                                    defaultValue={null}
                                                    render={({field}) => (
                                                        <TeamSelect value={field.value}
                                                                    onChange={val => field.onChange(val)}/>
                                                    )}
                                        />
                                    </div>
                                    <PrimaryButton>
                                        {
                                            joinMutation.isLoading ? (
                                                <Spinner/>
                                            ) : (
                                                <div>
                                                    Участвовать
                                                </div>
                                            )
                                        }
                                    </PrimaryButton>
                                </form>
                                {
                                    tournament?.participants.length > 0 ? (
                                        <div className={'mt-4 flex flex-col gap-4'}>
                                            <div className="overflow-x-auto shadow-lg sm:rounded-2xl">
                                                <table className="w-full text-sm text-left text-gray-500">
                                                    <thead
                                                        className="text-xs text-gray-700 uppercase bg-gray-50">
                                                    <Tr>
                                                        <Th>
                                                            Название команды
                                                        </Th>
                                                        <Th>
                                                            Капитан
                                                        </Th>
                                                        <Th>

                                                        </Th>
                                                    </Tr>
                                                    </thead>
                                                    <tbody>
                                                    {
                                                        tournament?.participants.map(team => {
                                                            return (
                                                                <Tr className="bg-white border-b odd:bg-white even:bg-gray-50"
                                                                    key={team.id}>
                                                                    <ThTd>
                                                                        {team.name}
                                                                    </ThTd>
                                                                    <Td>
                                                                        {team.captain.username}
                                                                    </Td>
                                                                    <Td>
                                                                    </Td>
                                                                </Tr>
                                                            )
                                                        })
                                                    }
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    ) : (
                                        <div>
                                            Пока никто не участвует :)
                                        </div>
                                    )
                                }
                            </div>
                        </>
                    )
                }
            </div>
        </Page>
    </App>)
}