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
    return useMutation({
        mutationFn: (data) => api.post("/auth/signUp", {username: data.login, password: data.password}),
        onSuccess: async (response) => {
            console.log(response.data)
            setUser({
                id: response.data.id,
                login: response.data.username,
            })
            await queryClient.invalidateQueries([])
        }
    })
}

export const Registration = () => {
    const navigate = useNavigate()
    const registrationMutation = useRegistrationMutation()
    const {register, handleSubmit, watch, formState: {errors}} = useForm();
    console.log(errors)
    const onSubmit = (data) => {
        console.log(data)
        registrationMutation.mutateAsync(data).then(() => {
            navigate('/profile')
        })
    }

    return (
        <App>
            <Header/>
            <Page>
                <div className={'max-w-[500px] mx-auto pt-[100px] md:pt-[200px]'}>
                    <div className={'p-8 rounded-3xl bg-white shadow-md'}>
                        <form className={'flex flex-col gap-8'} onSubmit={handleSubmit(onSubmit)}>
                            <h1 className={'font-bold text-3xl text-center'}>
                                Регистрация
                            </h1>
                            <Input type={'text'} placeholder={'Логин'} {...register('login', {required: true})}/>
                            <Input type={'password'}
                                   placeholder={'Пароль'} {...register('password', {required: true})}/>
                            <Input type={'password'}
                                   placeholder={'Повторите пароль'} {...register('password_confirmation', {
                                required: true, validate: (val) => {
                                    if (watch('password') !== val) {
                                        return "Пароли не совпадают";
                                    }
                                }
                            })}/>

                            <PrimaryButton disabled={registrationMutation.isLoading}>
                                <span className={'text-lg font-medium'}>
                                    Зарегистрироваться
                                </span>
                                <span>
                                    {
                                        registrationMutation.isLoading ? <Spinner/> :
                                            <ChevronDoubleRightIcon className="h-6 w-6 text-gray-500 stroke-[3]"/>
                                    }
                                </span>
                            </PrimaryButton>
                            <span className={'text-sm text-red-600'}>
                                {
                                    Object.keys(errors).map(error => {
                                        return error.type
                                    }).join(' ')
                                }
                            </span>
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