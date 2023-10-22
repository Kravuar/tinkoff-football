import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {useParams} from "react-router-dom";
import {forwardRef, useLayoutEffect, useMemo, useRef, useState} from "react";
import clsx from "clsx";
import {PrimaryButton, WhiteButton} from "../components/Button.jsx";
import {ArrowPathIcon} from "@heroicons/react/24/outline/index.js";
import {ActionsContainer, Modal, Title} from "../components/modal/Modal.jsx";
import {Input} from "../components/Input.jsx";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {api} from "../api.js";
import {useForm} from "react-hook-form";
import {Spinner} from "../components/Spinner.jsx";

//
// const nodes = [
//     {
//         name: "column1",
//         elements: [
//             {id: 1, player1: {name: "team1", value: 1}, player2: {name: "team2", value: 2}, next: 5},
//             {id: 2, player1: {name: "team3", value: 1}, player2: {name: "team4", value: 3}, next: 5},
//             {id: 3, player1: {name: "team22", value: null}, player2: {name: "team11", value: null}, next: 6},
//             {id: 4, player1: {name: "team33", value: null}, player2: {name: "team10", value: null}, next: 6},
//         ]
//     },
//     {
//         name: "column3",
//         elements: [
//             {id: 5, player1: {name: "team2", value: 3}, player2: {name: "team4", value: 2}, next: 7},
//             {id: 6, player1: {name: "???", value: null}, player2: {name: "???", value: null}, next: 7},
//         ]
//     },
//     {
//         name: "column4",
//         elements: [
//             {id: 7, player1: {name: "team2", value: null}, player2: {name: "???", value: null}, next: 8},
//         ]
//     },
//     {
//         name: "column5",
//         elements: [
//             {id: 8, player1: {name: "winner", value: null}, next: null},
//         ]
//     },
// ]


const Player = ({children}) => {
    return (
        <div className={'ps-2 pe-1 flex justify-between text-gray-900 bg-gray-normal'}>
            {children}
        </div>
    )
}
const Root = forwardRef(function Root({className, ...props}, ref) {
    return (
        <div ref={ref}
             className={clsx(className, `cursor-pointer flex flex-col border-e-primary border-e-4 border-s-primary border-s-4 gap-[0.8px] w-[240px] shadow-2xl`)}
             {...props}
        />
    )
})

const Brick = {
    Root, Player
}

const Column = forwardRef(function Column({className, style, ...props}, ref) {
    return (
        <div ref={ref} className={clsx(className, 'flex flex-col gap-16 justify-evenly')}
             style={{
                 ...style
             }}
             {...props}
        />
    )
})

const Node = ({tournament_id, node, nodeCb}) => {
    const client = useQueryClient()
    const [open, setOpen] = useState(false)
    const updateMutation = useMutation(['tournament-grid', tournament_id],
        (scores) => api.post(`/tournaments/updateScore/${node.id}`, scores), {
            onSettled: () => {
                client.invalidateQueries(['tournament-grid', tournament_id])
            }
        })
    const {register, handleSubmit} = useForm()
    const onClose = (e) => {
        setOpen(false)
    }
    const onSubmit = data => {
        onClose()
        updateMutation.mutate(data)
    }
    return (
        <Brick.Root onClick={() => setOpen(true)} key={node.id} ref={(el) => nodeCb(node, el)}>
            <Brick.Player>
                <div>
                    {node.team1DTO?.name ?? '???'}
                </div>
                <div>
                    {node.team1DTO?.score}
                </div>
            </Brick.Player>
            <Brick.Player>
                <div>
                    {node.team2DTO?.name ?? '???'}
                </div>
                <div>
                    {node.team2DTO?.score}
                </div>
            </Brick.Player>
            <Modal isOpen={open} onClose={onClose}>
                <Title>
                    Результаты матча {node.team1DTO?.name} - {node.team2DTO?.name}
                </Title>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <div className={'mt-4 flex flex-col gap-4'}>
                        <Input placeholder={'Счет 1 команды'} {...register('team1Score')}/>
                        <Input placeholder={'Счет 2 команды'} {...register('team2Score')}/>
                    </div>
                    <ActionsContainer>
                        <WhiteButton type={'button'} onClick={onClose}>
                            Отмена
                        </WhiteButton>
                        <PrimaryButton type={'submit'}>
                            Сохранить
                        </PrimaryButton>
                    </ActionsContainer>
                </form>
            </Modal>
        </Brick.Root>
    )
}

export const TournamentGrid = () => {
    const {id} = useParams()

    const client = useQueryClient()
    const {data, isLoading, isFetching, isError} = useQuery(['tournament-grid', id],
        async () => (await api.get(`/tournaments/${id}/bracket`)).data)

    const nodes = useMemo(() => {
        if (!data) return
        data.matches = data.matches.sort((match1, match2) => {
            return match2.bracketPosition - match1.bracketPosition
        })
        data.matches.forEach(match => {
            const next = Math.floor((match.bracketPosition - 1) / 2)
            const nextMatch = data.matches.find(m => m.bracketPosition === next)
            match.next = nextMatch?.id
        })
        console.log('data', data)
        let columns = []
        data.matches.forEach(match => {
            const col_idx = Math.floor(Math.log2(match.bracketPosition + 1))
            if (columns[col_idx])
                columns[col_idx].push(match)
            else
                columns[col_idx] = [match]
        })
        columns = columns.reverse()
        console.log('cols', columns)
        return columns
    }, [data])


    const nodesRef = useRef(new Map())
    const containerElement = useRef()
    const [lines, setLines] = useState([])
    useLayoutEffect(() => {
        if (!nodes || nodes.length === 0 || isLoading || isError) return
        const _lines = []
        const containerRect = containerElement.current.getBoundingClientRect()
        nodes?.forEach(col => {
            col.forEach(({id, next}) => {
                if (!next) return
                const selfRef = nodesRef.current.get(id)
                const nextRef = nodesRef.current.get(next)
                const selfRect = selfRef.getBoundingClientRect()
                const nextRect = nextRef.getBoundingClientRect()
                _lines.push({
                    x1: selfRect.right - containerRect.x,
                    y1: selfRect.top + selfRect.height / 2 - containerRect.y,
                    x2: nextRect.left - containerRect.x,
                    y2: nextRect.top + nextRect.height / 2 - containerRect.y,
                    id: `${id}-${next}`
                })
            })
        })
        console.log('lines', _lines)
        setLines(_lines)
    }, [nodes])

    const nodeCb = (node, element) => {
        // console.log(node, element)
        if (element) {
            nodesRef.current.set(node.id, element)
        } else {
            nodesRef.current.delete(node.id)
        }
    }

    const onUpdate = () => {
        console.log('update requested')
        client.invalidateQueries(['tournament-grid', id])
    }
    return (
        <App>
            <Header/>
            <Page>
                <div className={'pt-[80px]'}>
                    <PrimaryButton onClick={onUpdate}>
                        <ArrowPathIcon className={clsx("h-6 w-6 text-gray-500 stroke-2", isFetching ? 'animate-spin' : null)}/>
                    </PrimaryButton>
                    {
                        isLoading || isError ? (
                            isLoading ? (
                                <div className={'w-full flex justify-center items-center'}>
                                    <Spinner/>
                                </div>
                            ) : (
                                <div>
                                    Ошибка загрузки турнирной сетки
                                </div>
                            )
                        ) : (
                            <div className={'mt-10 min-w-full overflow-x-scroll'}>
                                <div className={'relative h-[70vh] overflow-x-scroll'}>
                                    <svg className={'absolute inset-0 w-full h-full text-primary z-0'}>
                                        {
                                            lines.map(line => {
                                                return (
                                                    <line key={line.id} x1={line.x1} y1={line.y1} x2={line.x2}
                                                          y2={line.y2}
                                                          stroke={'currentColor'} strokeWidth={"2"}/>
                                                )
                                            })
                                        }
                                    </svg>
                                    <div ref={containerElement} className={'absolute p-8 flex gap-8 z-10'}>
                                        {
                                            nodes.map(col => {
                                                return (
                                                    <Column key={col}>
                                                        {
                                                            col.map(node => {
                                                                return <Node tournament_id={id} key={node.id}
                                                                             node={node} nodeCb={nodeCb}/>
                                                            })
                                                        }
                                                    </Column>
                                                )
                                            })
                                        }
                                    </div>
                                </div>
                            </div>
                        )
                    }
                </div>
            </Page>
        </App>
    )
}