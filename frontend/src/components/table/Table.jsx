import clsx from "clsx";

export const Tr = ({className, ...props}) => {
    return <tr className={clsx(className, '')} {...props}/>
}

export const Th = ({className, ...props}) => {
    return <th scope="col" className={clsx(className, "px-6 py-3")} {...props} />
}

export const ThTd = ({className, ...props}) => {
    return <th scope="row"
               className={clsx(className, "px-6 py-4 font-medium text-gray-900 whitespace-nowrap")} {...props}/>
}

export const Td = ({className, ...props}) => {
    return <td scope="row"
               className={clsx(className, "px-6 py-4")} {...props}/>
}
