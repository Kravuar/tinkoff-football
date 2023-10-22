import {useEffect, useRef, useState} from "react";


function f() {
    new EventSource()
}

class EventsManager {
    sources

    constructor() {
        this.sources = []
    }

    connect(url) {

    }

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