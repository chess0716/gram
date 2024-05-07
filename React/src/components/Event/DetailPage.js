import React from "react";
import { useParams } from "react-router-dom";
import data from "../../constants/data";

const DetailPage = () => {
    const { id } = useParams(); // URL에서 id 파라미터를 가져옴
    const work = data.works[id]; // id에 해당하는 작업 데이터를 가져옴

    return (
        <div>
            <h1>{work.title}</h1>
            <img src={work.img} alt={work.title} />
            <p>{work.description}</p>
        </div>
    );
}

export default DetailPage;
