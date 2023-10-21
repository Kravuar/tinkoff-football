import {App} from "../components/App.jsx";
import {Header} from "../components/Header.jsx";
import {Page} from "../components/Page.jsx";
import {useParams} from "react-router-dom";
import {forwardRef, useLayoutEffect, useRef, useState} from "react";
import clsx from "clsx";
import {PrimaryButton} from "../components/Button.jsx";
import {ArrowPathIcon} from "@heroicons/react/24/outline/index.js";


const nodes = [
    {
        name: "column1",
        elements: [
            {id: 1, player1: {name: "aboba1", value: 1}, player2: {name: "aboba2", value: 1}, next: 5},
            {id: 2, player1: {name: "aboba1", value: 1}, player2: {name: "aboba2", value: 1}, next: 5},
            {id: 3, player1: {name: "aboba1", value: 1}, player2: {name: "aboba2", value: 1}, next: 6},
            {id: 4, player1: {name: "aboba1", value: 1}, player2: {name: "aboba2", value: 1}, next: 6},
        ]
    },
    {
        name: "column3",
        elements: [
            {id: 5, player1: {name: "???", value: 3}, player2: {name: "???", value: 3}, next: 7},
            {id: 6, player1: {name: "???", value: 3}, player2: {name: "???", value: 3}, next: 7},
        ]
    },
    {
        name: "column4",
        elements: [
            {id: 7, player1: {name: "???", value: 3}, player2: {name: "???", value: 3}, next: 8},
        ]
    },
    {
        name: "column5",
        elements: [
            {id: 8, player1: {name: "winner", value: null}, next: null},
        ]
    },
]


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
             className={clsx(className, `flex flex-col border-e-primary border-e-4 border-s-primary border-s-4 gap-[0.8px] w-[240px] shadow-2xl`)}
             {...props}
        />
    )
})

const Brick = {
    Root, Player
}

const Column = forwardRef(function Column({className, count, style, ...props}, ref) {
    return (
        <div ref={ref} className={clsx(className, 'flex flex-col gap-16 justify-evenly')}
             style={{
                 ...style
             }}
             {...props}
        />
    )
})


export const TournamentGrid = () => {
    const nodesRef = useRef(new Map())
    const containerElement = useRef()
    const [lines, setLines] = useState([])
    const params = useParams()
    useLayoutEffect(() => {
        const _lines = []
        const containerRect = containerElement.current.getBoundingClientRect()
        nodes.forEach(({elements}) => {
            elements.forEach(({id, next}) => {
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
    }
    return (
        <App>
            <Header/>
            <Page>
                <div className={'pt-[80px]'}>
                    <PrimaryButton onClick={onUpdate}>
                        <ArrowPathIcon className="h-6 w-6 text-gray-500 stroke-2"/>
                    </PrimaryButton>
                    <div className={'mt-10 min-w-full min-h-screen overflow-x-scroll'}>
                        <div className={'relative h-[70vh] overflow-x-scroll'}>
                            <div ref={containerElement} className={'p-8 flex gap-8'}>
                                {
                                    nodes.map((col, count) => {
                                        return (
                                            <Column key={col.name} count={count}>
                                                {
                                                    col.elements.map(node => {
                                                        return (
                                                            <Brick.Root key={node.id} ref={(el) => nodeCb(node, el)}>
                                                                <Brick.Player>
                                                                    <div>
                                                                        {node.player1.name}
                                                                    </div>
                                                                    <div>
                                                                        {node.player1.value}
                                                                    </div>
                                                                </Brick.Player>
                                                                {
                                                                    node.player2 ? (
                                                                        <Brick.Player>
                                                                            <div>
                                                                                {node.player2.name}
                                                                            </div>
                                                                            <div>
                                                                                {node.player2.value}
                                                                            </div>
                                                                        </Brick.Player>
                                                                    ) : null
                                                                }
                                                            </Brick.Root>
                                                        )
                                                    })
                                                }
                                            </Column>
                                        )
                                    })
                                }
                            </div>
                            <svg className={'absolute inset-0 w-full h-full text-primary'}>
                                {
                                    lines.map(line => {
                                        return (
                                            <line key={line.id} x1={line.x1} y1={line.y1} x2={line.x2} y2={line.y2}
                                                  stroke={'currentColor'} strokeWidth={"2"}/>
                                        )
                                    })
                                }
                            </svg>
                        </div>
                    </div>
                </div>
            </Page>
        </App>
    )
}