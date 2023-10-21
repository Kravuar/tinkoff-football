import {Page} from "../components/Page.jsx";
import {Header} from "../components/Header.jsx";
import {App} from "../components/App.jsx";
import background from "../assets/landing-background.jpg";
import football_gray from "../assets/football-gray.png";
import gray_light2 from "../assets/gray-light2.png";
import gray_light_cropped from "../assets/gray-light-cropped.png";
import soccer from "../assets/table_soccer_nobg.png";
import soccer_gradient from "../assets/table_soccer_nobg_gradient.png";
import soccer_shadow from "../assets/table_soccer_nobg_shadow.png";
import soccer_nolegs from "../assets/table_soccer_nolegs.png";
import {PrimaryButton} from "../components/Button.jsx";
import {Link} from "react-router-dom";

function Root() {
    return (
        <App>
            <Header/>
            <Page>
                <div className={'flex flex-col items-center pb-[100px]'}>
                    <div className={'mt-20 flex flex-col items-center'}>
                        <h1 className="text-5xl font-bold text-center">
                            Турниры по настольному футболу
                        </h1>
                        <h2 className={'mt-8 text-center'}>
                            Простой способ сохранить и приумножить сбережения. Понятные тарифы и удобное приложение
                        </h2>
                    </div>
                    <Link to={'/authorization'}>
                        <PrimaryButton className={'mt-8'}>
                            Попробовать бесплатно
                        </PrimaryButton>
                    </Link>
                    <div className={' max-w-full h-auto overflow-hidden'}>
                        <img className={'mt-8 rounded-2xl overflow-hidden object-cover object-top max-w-full h-auto'} src={soccer_nolegs} alt={''}/>
                    </div>
                </div>
            </Page>
        </App>
    )
}

export default Root
