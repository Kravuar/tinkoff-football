import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {PlayIcon, XMarkIcon} from "@heroicons/react/24/outline/index.js";
import {
    ArrowPathIcon,
    CalendarDaysIcon,
    ChevronLeftIcon,
    ChevronRightIcon,
    TrophyIcon
} from "@heroicons/react/24/outline";
import {PrimaryButton, WhiteButton} from "../components/Button.jsx";
import {Link} from "react-router-dom";
import {Td, Th, ThTd, Tr} from "../components/table/Table.jsx";
import {useQuery, useQueryClient} from "@tanstack/react-query";
import {api} from "../api.js";
import {useState} from "react";
import {Spinner} from "../components/Spinner.jsx";
import clsx from "clsx";

// const tournaments = [
//     {name: 'Big tournament', owner: "danil#4122", status: "active"},
//     {name: 'Small tournament', owner: "andrew#1122", status: "finished"},
//     {name: 'Medium tournament', owner: "roman#1122", status: "opened"},
//     {name: 'Big tournament', owner: "danil#4122", status: "active"},
//     {name: 'Small tournament', owner: "andrew#1122", status: "finished"},
//     {name: 'Medium tournament', owner: "roman#1122", status: "opened"},
//     {name: 'Big tournament', owner: "danil#4122", status: "active"},
//     {name: 'Small tournament', owner: "andrew#1122", status: "finished"},
//     {name: 'Medium tournament', owner: "roman#1122", status: "opened"},
//     {name: 'Big tournament', owner: "danil#4122", status: "active"},
//     {name: 'Small tournament', owner: "andrew#1122", status: "finished"},
//     {name: 'Medium tournament', owner: "roman#1122", status: "opened"},
// ].map((obj, id) => {
//     obj.id = id
//     return obj
// })

const Table = () => {
    const client = useQueryClient()
    const [page, setPage] = useState(0)
    const {data, isLoading, isError, isFetching} = useQuery(['tournaments', page],
        async () => (await api.get(`/tournaments/list/10/${page}`)).data)
    const onUpdate = () => {
        client.invalidateQueries(['tournaments'])
    }
    console.log(data)
    const prevPage = () => {
        if (page > 0)
            setPage(page => page - 1)
    }
    const nextPage = () => {
        if (data?.totalPages && page < data?.totalPages - 1)
            setPage(page => page + 1)
    }

    return (
        <div
            className={'mt-14 w-full p-3 md:p-8 border-t-gray-600 border-t-[5px] bg-white mx-auto rounded-3xl shadow-2xl'}>
            <div className={'flex justify-end items-center gap-4'}>
                {
                    data?.totalTournaments ? (
                        <div>
                            Турниров: {data?.totalTournaments}
                        </div>
                    ) : null
                }
                <div>
                    {
                        data?.totalPages ? (
                            <div>
                                Страниц: {data?.totalPages}
                            </div>
                        ) : null
                    }
                </div>

                <div className={'flex items-center rounded-lg'}>
                    <button onClick={prevPage} className={'p-2 md:p-4 bg-gray-200 hover:bg-gray-300 rounded-s-lg'}>
                        <ChevronLeftIcon className={'h-6 w-6 text-gray-900'}/>
                    </button>
                    <div className={'p-2 md:p-4 bg-gray-200 hover:bg-gray-300'}>
                        {page}
                    </div>
                    <button onClick={nextPage} className={'p-2 md:p-4 bg-gray-200 hover:bg-gray-300 rounded-e-lg'}>
                        <ChevronRightIcon className={'h-6 w-6 text-gray-900'}/>
                    </button>
                </div>
                <PrimaryButton onClick={onUpdate}>
                    <ArrowPathIcon className={clsx("h-4 md:h-6 w-4 md:w-6 text-gray-500 stroke-2", isLoading || isFetching ? "animate-spin" : null)}/>
                </PrimaryButton>
            </div>
            <div className={'mt-4 flex flex-col gap-4'}>
                {
                    isError || isLoading ? (
                        isLoading ? (
                            <div className={'w-full flex justify-center items-center'}>
                                <Spinner/>
                            </div>
                        ) : (
                            <div>
                                Ошибка загрузки турниров
                            </div>
                        )
                    ) : (
                        <div className="overflow-x-auto shadow-lg sm:rounded-2xl">
                            <table className="w-full text-sm text-left text-gray-500">
                                <thead
                                    className="text-xs text-gray-700 uppercase bg-gray-50">
                                <Tr>
                                    <Th>
                                        Название турнира
                                    </Th>
                                    <Th>
                                        Организатор
                                    </Th>
                                    <Th>
                                        Статус
                                    </Th>
                                </Tr>
                                </thead>
                                <tbody>
                                {
                                    data?.tournaments?.map(tournament => {
                                        return (
                                            <Tr className="bg-white border-b odd:bg-white even:bg-gray-50"
                                                key={tournament.id}>
                                                <ThTd>
                                                    <Link to={`/tournaments/${tournament.id}`}>
                                                        {tournament.title}
                                                    </Link>
                                                </ThTd>
                                                <Td>
                                                    {tournament.owner.username}
                                                </Td>
                                                <Td>
                                                    {
                                                        {
                                                            PENDING: <CalendarDaysIcon title={'Открыт'}
                                                                                       className={"h-6 w-6 text-gray-500 stroke-2"}/>,
                                                            ACTIVE: <PlayIcon title={'Продолжается'}
                                                                              className={'h-6 w-6 text-green-500 stroke-2'}/>,
                                                            FINISHED: <TrophyIcon title={'Завершен'}
                                                                                  className={'h-6 w-6 text-primary stroke-2'}/>,
                                                            CANCELED: <XMarkIcon title={'Отменен'}
                                                                                 className={'h-6 w-6 text-primary stroke-2'}/>,
                                                        }[tournament.status]
                                                    }
                                                </Td>
                                            </Tr>
                                        )
                                    })
                                }
                                </tbody>
                            </table>
                        </div>
                    )
                }
            </div>
        </div>
    )
}

export const Tournaments = () => {
    const onUpdate = () => {
        console.log('update tournaments requested')
    }
    return (
        <App>
            <Header/>
            <Page>
                <div className={'pt-[100px] md:pt-[200px] flex flex-col items-center'}>

                    <div className={'p-3'}>
                        <h1 className={'text-[44px] font-bold'}>
                            Все турниры
                        </h1>
                    </div>
                    <Link to={'/tournaments-create'}>
                        <PrimaryButton className={'mt-8'}>
                            Создать турнир
                        </PrimaryButton>
                    </Link>
                    <Table/>
                </div>
            </Page>
        </App>
    )
}