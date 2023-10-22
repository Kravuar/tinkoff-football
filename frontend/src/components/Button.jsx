import {forwardRef} from "react";
import clsx from "clsx";

export const Button = forwardRef(function Button({className, ...props}, ref) {
    return (
        <button
            {...props}
            ref={ref}
            className={clsx('transition-colors duration-500 p-3 md:p-4 rounded-lg flex items-center justify-center gap-2', className)}
        />
    )
})

export const PrimaryButton = forwardRef(function PrimaryButton({className, ...props}, ref) {
    return (
        <Button
            {...props}
            ref={ref}
            className={clsx('bg-primary hover:bg-primary-dark', className)}
        />
    )
})

export const WhiteButton = forwardRef(function WhiteButton({className, ...props}, ref) {
    return (
        <Button
            {...props}
            ref={ref}
            className={clsx('bg-gray-200 hover:bg-gray-300', className)}
        />
    )
})