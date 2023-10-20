import clsx from 'clsx'
import {forwardRef} from "react";

export const Input = forwardRef(function Input({className, ...props}, ref) {
    return (
        <input {...props} ref={ref}
               className={clsx('p-4 rounded-lg bg-gray-normal placeholder:text-gray-600 focus:bg-gray-50 transition-colors duration-500 outline-gray-400', className)}
        />
    )
})