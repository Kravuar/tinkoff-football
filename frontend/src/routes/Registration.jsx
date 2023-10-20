import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {ChevronDoubleRightIcon} from "@heroicons/react/24/outline";
import {Input} from "../components/Input.jsx";
import {useForm} from "react-hook-form";

export const Registration = () => {
    const {register, handleSubmit, watch, formState: {errors}} = useForm();

    const onSubmit = (data) => {
        console.log(data)
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

                            <button className={'bg-primary hover:bg-primary-dark transition-colors duration-500 p-4 rounded-lg flex items-center justify-center gap-2'}>
                                <span className={'text-lg font-medium'}>
                                    Зарегистрироваться
                                </span>
                                <span>
                                    <ChevronDoubleRightIcon class="h-6 w-6 text-gray-500 stroke-[3]"/>
                                </span>
                            </button>
                        </form>
                    </div>
                </div>
            </Page>
        </App>
    )
}