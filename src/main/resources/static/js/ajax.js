$("input[type='checkbox']").on("click", fire_ajax_submit);
fire_ajax_submit();


function fire_ajax_submit() {

    var search = $("#my_filter").serialize();
    // $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/search",
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var hardwares = data.result;
            var hardwareHtml = "";
            var currency = data.currency;
            var cur = 0;
            hardwares.forEach(function (hardware) {
                var characteristicHtml = "";
                var i = 0;
                var j = 5;
                var characteristics = hardware["characteristics"]
                while (i < characteristics.length) {
                    if (i >= j) {
                        break;
                    }
                    characteristicHtml += characteristics[i]["nameCharacteristic"] + ": " + characteristics[i]["valueCharacteristic"];
                    if (i === characteristics.length - 1 || i === j - 1) {
                        characteristicHtml += ". ";
                    } else {
                        characteristicHtml += ", ";
                    }

                    i++;
                }

                hardwareHtml +=
                    "<div class=\"col-md-4  pd-10\">" +
                    "                            <div class=\"one-product\">" +
                    "                                <div class=\"thumbnail\">" +
                    "                                    <a href=\"/catalog/good?hardwareId=" + hardware["id"] + "\">" +
                    "                                        <img src=\"data:image/png;base64," + hardware["imageHardware"] + "\"\n" +
                    "                                             class=\"pd-10 w-100\" height=\"200\">" +
                    "                                    </a>" +
                    "\n" +
                    "                                    <div class=\"caption\">" +
                    "                                        <div class=\"row\">" +
                    "                                            <div class=\"col-md-12 text-center col-xs-6\">" +
                    "                                                <h4 class=\"name-product\">" +
                    "                                                    <a href=\"catalog/good?hardwareId=" + hardware["id"] + "\">" + hardware["nameHardware"] + "" +
                    "                                                    </a>" +
                    "                                                </h4>" +
                    "<h6 class=\"characteristics\">" + characteristicHtml + "</h6>" +
                    "                                            </div>" +
                    "                                            <div class=\"col-md-12 col-xs-6 price\">" +
                    "                                                <h4 class=\"text-center\">\n" +
                    "                                                    <label>" + hardware["priceHardware"] + " РУБ</label></h4>" +
                    "                                        <div class=\"text-center\">\n" +
                    "                                            <label class=\"badge badge-primary text-wrap\" style=\"padding: 0.65em .6em;background-color: black;\">"
                    + currency[cur] + " USD</label>\n" +
                    "                                        </div>" +
                    "                                            </div>" +

                    "                                        </div>" +
                    "                                        <div class=\"row\">" +
                    "                                            <div class=\"col-md-12\" style=\"padding-top: 5%;\">" +
                    "                                                <form method=\"post\" action=\"catalog/add?hardwareId=" + hardware["id"] + "\" " +
                    "                                              style=\"display:inline;\">" +
                    "                                                  <input type=\"hidden\"  name=\"count\" value=\"1\">" +
                    "                               <button class=\"btn btn-success btn-product float-right\" type=\"submit\">" +
                    "                                                                                   В корзину</button>" +
                    "                                                </form>" +
                    "                                            </div>" +
                    "                                        </div>" +
                    "                                    </div>" +
                    "                                </div>" +
                    "                            </div>" +
                    "                        </div>";
                cur++;
            });
            $('#feedback').html(hardwareHtml);
            console.log("SUCCESS : ", data);

        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);

        }
    });

}