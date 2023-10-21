import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {PrimaryButton} from "../components/Button.jsx";
import {Input} from "../components/Input.jsx";
import {useForm} from "react-hook-form";
import {data} from "autoprefixer";
import {useMutation} from "@tanstack/react-query";
import {api} from "../api.js";
import {useNavigate} from "react-router-dom";

export const TournamentCreate = () => {
    const navigate = useNavigate()
    const mutation = useMutation(['tournaments'],
        (data) => api.post("/tournaments/create", data))

    const {register, handleSubmit} = useForm()
    const onSubmit = (data) => {
        console.log(data)
        const withMatches = {
            ...data,
            startDateTime: new Date(),
            matches: Array.from({length: 3}, () => ({
                prize: "CRINGE",
                bestOf: 5
            })),
        }
        mutation.mutateAsync(withMatches).then(() => {
            navigate('/tournaments')
        })
    }
    return (
        <App>
            <Header/>
            <Page>
                <div className={'max-w-[500px] mx-auto pt-[100px] md:pt-[200px]'}>
                    <div className={'p-8 rounded-3xl bg-white shadow-md'}>
                        <h1 className={'font-bold text-3xl text-center'}>
                            Создание турнира
                        </h1>
                        <form className={'mt-8'} onSubmit={handleSubmit(onSubmit)}>
                            <div className={'flex items-center gap-2'}>
                                <Input className={'w-full'} placeholder={'Название'} {...register('title')}/>
                                <PrimaryButton className={'whitespace-nowrap'}>
                                    Сохранить
                                </PrimaryButton>
                            </div>
                        </form>
                    </div>
                </div>
            </Page>
        </App>
    )
}