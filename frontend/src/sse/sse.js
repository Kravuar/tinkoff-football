import {useEffect, useRef, useState} from "react";

function f() {
    const source = new EventSource('/api/tournaments/1/bracket/subscribe')
    source.onmessage = ev => {
        console.log(ev.data)
    }
    source.addEventListener('score-update', ev => {
        console.log('score-update', ev.data)
    })
}

const useEvents = (url) => {
    const [events, setEvents] = useState([])
    useEffect(() => {
        const source = new EventSource(url)
        source.onmessage = ev => {
            setEvents(events => [...events, ev.data])
        }
        return () => {
            source.close()
        }
    }, [url])
}