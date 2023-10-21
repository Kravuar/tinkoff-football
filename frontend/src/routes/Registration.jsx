import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {ChevronDoubleRightIcon} from "@heroicons/react/24/outline";
import {Input} from "../components/Input.jsx";
import {useForm} from "react-hook-form";
import {PrimaryButton} from "../components/Button.jsx";
import {Link, useNavigate} from "react-router-dom";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {useUser} from "../hooks/useUser.jsx";
import {api} from "../api.js";
import {Spinner} from "../components/Spinner.jsx";

const useRegistrationMutation = () => {
    const queryClient = useQueryClient()
    const setUser = useUser(state => state.setUser)
    const navigate = useNavigate()
    return useMutation({
        mutationFn: (data) => api.post("/auth/registration", data),
        onSuccess: async (response) => {
            setUser(response.data)
            await queryClient.invalidateQueries([])
            navigate('/')
        }
    })
}

export const Registration = () => {
    const registrationMutation = useRegistrationMutation()
    const {register, handleSubmit, watch, formState: {errors}} = useForm();

    const onSubmit = (data) => {
        console.log(data)
        // registrationMutation.mutate(data)
    }

    return (
        <App>
            <Header/>
            <Page>
                <div className={'max-w-[500px] mx-auto pt-[250px] pb-[300px]'}>
                    <div className={'p-8 rounded-3xl bg-white shadow-md'}>
                        <form className={'flex flex-col gap-8'} onSubmit={handleSubmit(onSubmit)}>
                            <h1 className={'font-bold text-3xl text-center'}>
                                Регистрация
                            </h1>
                            <Input type={'text'} placeholder={'Логин'} {...register('login', {required: true})}/>
                            <Input type={'password'}
                                   placeholder={'Пароль'} {...register('password', {required: true})}/>
                            <Input type={'password'}
                                   placeholder={'Повторите пароль'} {...register('password_confirmation', {required: true})}/>

                            <PrimaryButton disabled={registrationMutation.isLoading}>
                                <span className={'text-lg font-medium'}>
                                    Зарегистрироваться
                                </span>
                                <span>
                                    {
                                        registrationMutation.isLoading ? <Spinner/> : <ChevronDoubleRightIcon className="h-6 w-6 text-gray-500 stroke-[3]"/>
                                    }
                                </span>
                            </PrimaryButton>
                        </form>
                        <div className={'text-sm text-gray-400 text-center mt-3'}>
                                <Link to={'/authorization'}>
                                    Уже зарегистрированы?
                                </Link>
                        </div>
                    </div>
                </div>
            </Page>
        </App>
    )
}