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

    const onJoin = (data) => {
        console.log(data)
        joinMutation.mutate(data.team.id)
    }

    return (<App>
        <Header/>
        <Page>
            <div className={'mt-[100px] flex flex-col items-center w-full min-h-[85vh]'}>
                <h1 className={'font-bold text-[42px]'}>
                    Турнир "{tournament.name}#{id}"
                </h1>
                <div className={'mt-4'}>
                    {{
                        opened: "Турнир открыт", active: "Турнир продолжается", finished: "Турнир завершен"
                    }[tournament.status]}
                </div>
                {/* список команд */}
                <div>

                </div>
                <Link className={'mt-8'} to={`/tournaments/${id}/grid`}>
                    <PrimaryButton>
                        Открыть сетку
                    </PrimaryButton>
                </Link>

                <div className={'mt-12'}>
                    {/* принять участие */}
                    <form className={'w-[360px] flex gap-1 items-center'} onSubmit={handleSubmit(onJoin)}>
                        {/*  выбор команды  */}
                        <div className={'w-full'}>
                            <Controller rules={{required: true}} control={control} name={'team'}
                                        defaultValue={null}
                                        render={({field}) => (
                                            <TeamSelect value={field.value} onChange={val => field.onChange(val)}/>
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
                    {/* открыть сетку */}
                    <div>

                    </div>
                </div>
            </div>
        </Page>
    </App>)
}