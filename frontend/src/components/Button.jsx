import {forwardRef} from "react";
import clsx from "clsx";

export const Button = forwardRef(function Button({className, ...props}, ref) {
    return (
        <button
            {...props}
            ref={ref}
            className={clsx('bg-primary hover:bg-primary-dark transition-colors duration-500 p-4 rounded-lg flex items-center justify-center gap-2', className)}
        />
    )
})