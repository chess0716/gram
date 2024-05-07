import React, {useState} from "react";
import data from "../../constants/data";
// import images from "../../constants/images";
import "./Places.css";
// import {FaTimesCircle} from "react-icons/fa";
import { Link , useNavigate} from "react-router-dom";

const Work = () => {

    const [imageModal, setImageModal] = useState(false);
    const [imageSrc, setImageSrc] = useState("");

    const setImageOnModal = (src) => {
        setImageSrc(src);
        setImageModal(true);
    }
    const navigate = useNavigate(); // useNavigate 훅 추가

    return (
        <div className="work">
            {/* <div className={imageModal ? "image__modal image__modal__show" : "image__modal"}>
                <div className="image__modal--content">
                    <FaTimesCircle className="modal__close--btn text__light bg__blue" size = {30} onClick = {() => setImageModal(false)} />
                    <img src = {imageSrc} alt = "" />
                </div>
            </div> */}
            <div className="work__content grid">
                {data.works.map((work, index) => (
                    <div className="work__content--item" key={index} onClick={() => navigate(`/details/${index}`)}>
                        {/* onClick 이벤트로 페이지 이동 처리 */}
                        <img src={work.img} alt="" className="img" />
                    </div>
                ))}
            </div>

            <div className="work__content grid">
                {
                    data.works.map((work, index) => {
                        return (
                            <div className="work__content--item" key = {index} onClick = {() => setImageOnModal(work.img)}>
                                <div className="icon">
                                </div>
                            </div>
                        )
                    })
                }
            </div>
        </div>
    )
}

export default Work;