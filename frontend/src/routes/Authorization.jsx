import {useForm} from "react-hook-form";
import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {Input} from "../components/Input.jsx";
import {ChevronDoubleRightIcon} from "@heroicons/react/24/outline/index.js";
import {PrimaryButton} from "../components/Button.jsx";
import {Link, useNavigate} from "react-router-dom";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {useUser} from "../hooks/useUser.jsx";
import {api} from "../api.js";
import {Spinner} from "../components/Spinner.jsx";

const useLoginMutation = () => {
    const setUser = useUser(state => state.setUser)
    const queryClient = useQueryClient()
    return useMutation({
        mutationFn: (data) => api.post("/auth/signIn", {
            username: data.login,
            password: data.password
        }),
        onSuccess: async (response) => {
            console.log(response.data)
            setUser({
                id: response.data.id,
                login: response.data.username
            })
            await queryClient.invalidateQueries([])
        }
    })
}

export const Authorization = () => {
    const setUser = useUser(state => state.setUser)
    const navigate = useNavigate()
    const loginMutation = useLoginMutation()
    const {register, handleSubmit, watch, formState: {errors}} = useForm();

    const onSubmit = (data) => {
        console.log(data)
        loginMutation.mutateAsync(data).then(() => {
            navigate('/profile')
        })
    }

    return (
        <App>
            <Header/>
            <Page>
                <div className={'max-w-[500px] mx-auto pt-[250px] pb-[300px]'}>
                    <div className={'p-8 rounded-3xl bg-white shadow-md'}>
                        <form className={'flex flex-col gap-8'} onSubmit={handleSubmit(onSubmit)}>
                            <h1 className={'font-bold text-3xl text-center'}>
                                Авторизация
                            </h1>
                            <Input type={'text'} placeholder={'Логин'} {...register('login', {required: true})}/>
                            <Input type={'password'}
                                   placeholder={'Пароль'} {...register('password', {required: true})}/>

                            <PrimaryButton disabled={loginMutation.isLoading}>
                                <span className={'text-lg font-medium'}>
                                    Авторизоваться
                                </span>
                                <span>
                                    {
                                        loginMutation.isLoading ? <Spinner/> : <ChevronDoubleRightIcon className="h-6 w-6 text-gray-500 stroke-[3]"/>
                                    }
                                </span>
                            </PrimaryButton>
                        </form>
                        <div className={'text-sm text-gray-400 text-center mt-3'}>
                            <Link to={'/registration'}>
                                Не зарегистрированы?
                            </Link>
                        </div>
                    </div>
                </div>
            </Page>
        </App>
    )
}