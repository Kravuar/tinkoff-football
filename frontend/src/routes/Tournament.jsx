import {Link, useParams} from "react-router-dom";
import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {PrimaryButton} from "../components/Button.jsx";
import {Controller, useForm} from "react-hook-form";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {api} from "../api.js";
import Select from "react-select";
import AsyncSelect from "react-select/async";

const tournament = {
    name: "Большой турнир", status: "opened"
}

export const Tournament = () => {
    const {id} = useParams();
    const client = useQueryClient()
    const {register, control, handleSubmit} = useForm()
    const joinMutation = useMutation(['tournament', id], (team_id) => api.get(`/tournaments/${id}/join/${team_id}`), {
        onSettled: () => {
            client.invalidateQueries(['tournament', id])
        }
    })

    const onJoin = (data) => {
        console.log(data)
        // joinMutation.mutate(data.)
    }
    const options = [
        {id: 1, value: 1, label: 'team1'},
        {id: 2, value: 2, label: 'team2'},
        {id: 3, value: 3, label: 'team3'},
    ]

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
                    <form className={'flex gap-1 items-center'} onSubmit={handleSubmit(onJoin)}>
                        {/*  выбор команды  */}
                        <Controller rules={{required: true}} control={control} name={'team'}
                                    defaultValue={null}
                                    render={({field}) => (
                                        <AsyncSelect
                                            defaultOptions={options}
                                            loadOptions={() => api.get('/teams/my')}
                                            value={field.value}
                                            onChange={val => field.onChange(val)}
                                        />
                                    )}
                        />
                        <PrimaryButton className={'mt-4'}>
                            Участвовать
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