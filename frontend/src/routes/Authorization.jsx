import {useForm} from "react-hook-form";
import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {Input} from "../components/Input.jsx";
import {ChevronDoubleRightIcon} from "@heroicons/react/24/outline/index.js";
import {Button} from "../components/Button.jsx";
import {Link} from "react-router-dom";

export const Authorization = () => {
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
                                Авторизация
                            </h1>
                            <Input type={'text'} placeholder={'Логин'} {...register('login', {required: true})}/>
                            <Input type={'password'}
                                   placeholder={'Пароль'} {...register('password', {required: true})}/>

                            <Button>
                                <span className={'text-lg font-medium'}>
                                    Авторизоваться
                                </span>
                                <span>
                                    <ChevronDoubleRightIcon class="h-6 w-6 text-gray-500 stroke-[3]"/>
                                </span>
                            </Button>
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