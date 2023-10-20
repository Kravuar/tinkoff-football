import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import { ChevronDoubleRightIcon } from "@heroicons/react/24/outline";

export const Registration = () => {

    const handleSubmit = (e) => {
        e.preventDefault()
    }

    return (
        <App>
            <Header/>
            <Page>
                <div className={'max-w-[500px] mx-auto pt-[250px] pb-[300px]'}>
                    <div className={'p-8 rounded-3xl bg-white shadow-md'}>
                        <form className={'flex flex-col gap-8'} onSubmit={handleSubmit}>
                            <h1 className={'font-bold text-3xl text-center'}>
                                Регистрация
                            </h1>
                            <input placeholder={'Логин'} className={'p-4 rounded-lg bg-gray-normal placeholder:text-gray-600'}/>
                            <input placeholder={'Пароль'} className={'p-4 rounded-lg bg-gray-normal placeholder:text-gray-600'}/>
                            <input placeholder={'Повторите пароль'} className={'p-4 rounded-lg bg-gray-normal placeholder:text-gray-600'}/>
                            <button className={'bg-primary p-4 rounded-lg flex items-center justify-center gap-2'}>
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