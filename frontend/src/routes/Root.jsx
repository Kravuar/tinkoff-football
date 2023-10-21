import {Page} from "../components/Page.jsx";
import {Header} from "../components/Header.jsx";
import {App} from "../components/App.jsx";
import background from "../assets/landing-background.jpg";
import football_gray from "../assets/football-gray.png";
import gray_light2 from "../assets/gray-light2.png";
import gray_light_cropped from "../assets/gray-light-cropped.png";
import {PrimaryButton} from "../components/Button.jsx";

function Root() {
    return (
        <App>
            <Header/>
            <Page>
                <div className={'flex flex-col items-center pb-[100px]'}>
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
                    <div>
                        <img className={'mt-8 rounded-2xl overflow-hidden object-cover shadow-2xl'} src={gray_light_cropped} alt={''}/>
                    </div>
                </div>
            </Page>
        </App>
    )
}

export default Root
