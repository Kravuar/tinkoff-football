import {useQuery} from "@tanstack/react-query";
import {api} from "./api.js";

export const useTeams = (options) =>  useQuery(
    ['teams'],
    async () => (await api.get('/teams/my')).data,
    options
)