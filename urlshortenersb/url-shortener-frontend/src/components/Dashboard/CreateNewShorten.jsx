import React from 'react'
import { useForm } from 'react-hook-form';
import { useState } from 'react';
import { useStoreContext } from '../../contextApi/ContextApi';
import TextField from '../TextField';
import { RxCross2 } from 'react-icons/rx';
import Tooltip from '@mui/material/Tooltip'
import { toast } from 'react-hot-toast';
import api from '../../api/api';

const CreateNewShorten = ({setOpen, refetch}) => {
    const {token} = useStoreContext();
    const [loading, setLoading] = useState(false);

    const {
        register,
        handleSubmit,
        reset,
        formState: {errors},
    } = useForm({
        defaultValues: {
            originalUrl: "",
            expiryInDays: 7, // NEW FEATURE: expiry date - default to 7 days
    },
    mode: "onTouched",
  });

  const createShortUrlHandler = async (data) => {
    setLoading(true);
    try {
        const {data:res} = await api.post("/api/urls/shorten", data,{
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
                Authorization: "Bearer " + token,
            },
        });

        // REDIRECT FEATURE - use full short URL from backend instead of frontend domain
        const shortenUrl = res.fullShortUrl || `${import.meta.env.VITE_REACT_SUBDOMAIN}/${res.shortUrl}`;
        navigator.clipboard.writeText(shortenUrl).then(() => {
            toast.success("Short URL copied to clipboard!",{
                position:"bottom-center",
                className:"mb-5",
                duration:3000,
            });
    });

    //await refetch();
    reset();
    setOpen(false);
 } catch (error) {
        toast.error("Creating ShortURL failed");
    } finally {
        setLoading(false);
    }

  };

  return (
    <div className='flex justify-center items-center bg-white rounded-md'>
        <form 
        onSubmit={handleSubmit(createShortUrlHandler)}
        className="sm:w-[450px] w-[360px] relative shadow-lg bg-white pt-8 pb-6 sm:px-8 px-5 rounded-2xl">
            <h1 className="font-montserrat text-center font-bold sm:text-2xl text-xl text-slate-800">
                Create New Shorten Url
            </h1>
            <hr className='mt-2 sm:mb-5 mb-3 border-slate-200' />
            <div className="mb-6">
                <TextField
                    label="Enter URL"
                    required
                    id="originalUrl"
                    placeholder="https://example.com"
                    type="url"
                    message="Url is required"
                    register={register}
                    errors={errors}
                    className="w-full"
                />
            </div>
            {/* NEW FEATURE: expiry date - expiry input field */}
            <div className="mb-6">
                <TextField
                    label="Expiry in Days (Optional)"
                    id="expiryInDays"
                    placeholder="7"
                    type="number"
                    message="Enter number of days (1-365)"
                    register={register}
                    errors={errors}
                    className="w-full"
                    min="1"
                    max="365"
                />
            </div>
            <button className="bg-gradient-to-r from-blue-500 to-purple-500 text-white font-semibold px-6 py-2 rounded-md shadow-md transition-all duration-200 hover:opacity-90 disabled:opacity-50 disabled:cursor-not-allowed" type="submit">

                {loading?"Loading...":"Create"}
            </button>
            {!loading && (
            <Tooltip title="Close">
                <button disabled={loading}
                onClick={()=>setOpen(false)}
                className='absolute right-2 top-2'>
                <RxCross2 className="text-slate-800 text-3xl"/>
                </button>
                </Tooltip>
            )}

        </form>
    </div>
  )
}

export default CreateNewShorten