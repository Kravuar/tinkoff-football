import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {PrimaryButton} from "../components/Button.jsx";
import {Input} from "../components/Input.jsx";
import {useForm} from "react-hook-form";

export const TournamentCreate = () => {
    const {register, handleSubmit} = useForm()
    return (
        <App>
            <Header/>
            <Page>
                <div className={'max-w-[500px] mx-auto pt-[250px] pb-[300px]'}>
                    <div className={'p-8 rounded-3xl bg-white shadow-md'}>
                        <h1 className={'font-bold text-3xl text-center'}>
                            Создание турнира
                        </h1>
                        <form className={'mt-8'}>
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