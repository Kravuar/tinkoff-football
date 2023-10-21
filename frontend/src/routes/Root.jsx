import {Page} from "../components/Page.jsx";
import {Header} from "../components/Header.jsx";
import {App} from "../components/App.jsx";
import background from "../assets/landing-background.jpg";
import football_gray from "../assets/football-gray.png";
import {PrimaryButton} from "../components/Button.jsx";

function Root() {
    return (
        <App>
            <Header/>
            <Page>
                <div className={'flex flex-col items-center'}>
                    <div className={'mt-20 flex flex-col items-center'}>
                        <h1 className="text-5xl font-bold">
                            Турниры по настольному футболу
                        </h1>
                        <h2 className={'mt-8'}>
                            Простой способ сохранить и приумножить сбережения. Понятные тарифы и удобное приложение
                        </h2>
                    </div>
                    <PrimaryButton className={'mt-8'}>
                        Попробовать бесплатно
                    </PrimaryButton>
                    <img className={'mt-4'} src={football_gray} alt={''}/>
                </div>
            </Page>
        </App>
    )
}

export default Root
