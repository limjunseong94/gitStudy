/**
 * 
 */
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	    mapOption = {
	        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
	        level: 3 // 지도의 확대 레벨
	    };  
	
	// 지도를 생성합니다    
	var map = new kakao.maps.Map(mapContainer, mapOption); 
	// 일반 지도와 스카이뷰로 지도 타입을 전환할 수 있는 지도타입 컨트롤을 생성합니다
	var mapTypeControl = new kakao.maps.MapTypeControl();
	// 지도 타입 컨트롤을 지도에 표시합니다
	map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);
	// 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
	var zoomControl = new kakao.maps.ZoomControl();
	map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
	
	maps();
	
	function maps(){
	  var table =document.getElementById('table_map');
	  var rowList = table.rows;
	  
	  for (i=1; i<rowList.length; i++) {//thead부분 제외.
    
	      var row = rowList[i];
	    
	      row.onclick = function(){ 
	          return function(){ 
	          
	          //개별적으로 값 가져오기
	       
	        var  ranking =this.cells[0].innerText; 
	        var  names = this.cells[1].innerText; 
	        var  region = this.cells[2].innerText;
	        var  cate = this.cells[3].innerText;
	        var  phone = this.cells[4].innerText;	        
			var  address = this.cells[5].innerText;
	        var  search = this.cells[6].innerText;
	        var  number = this.cells[7].innerText; 
	
	// 주소-좌표 변환 객체를 생성합니다
	var geocoder = new kakao.maps.services.Geocoder();
	
	// 주소로 좌표를 검색합니다
	geocoder.addressSearch(address, function(result, status) {
	
	    // 정상적으로 검색이 완료됐으면 
	     if (status === kakao.maps.services.Status.OK) {
	
	        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

	        // 결과값으로 받은 위치를 마커로 표시합니다
	        var marker = new kakao.maps.Marker({
	            map: map,
	            position: coords,
	            clickable: true
	        });
        
	        kakao.maps.event.addListener(marker, 'click', function() {        
   	    
		        // 인포윈도우로 장소에 대한 설명을 표시합니다
		        var iwContent='<div style="width:250px;margin:4px;padding:6px 0;"><div style="font-size:22px;margin:10px 10px 1px;"><strong>'+names+
		        '</strong></div><br><div style="font-size:15px;margin:5px;">'+address+
		        '</div><div style="font-size:14px;margin:1px 10px 1px;"><span>'+phone+'</span><span style="margin:1px 10px 1px;"><a style="text-decoration:none;" href="/top/rest/detail/'+number+
		        '">상세보기</a></span></div><a style="display:flex;justify-content:center;" class="btn" href="https://map.kakao.com/link/to/'+names+','+
		        marker.getPosition().getLat()+','+marker.getPosition().getLng()+'" target="_blank">길찾기</a></div>';
		        var	iwRemoveable = true;
		        
		        var infowindow = new kakao.maps.InfoWindow({
		            content: iwContent,
		            removable : iwRemoveable
		        });
		        
		        infowindow.open(map, marker);  
		        		        
			});
			
			kakao.maps.event.addListener(marker, 'rightclick', function() {
				marker.setMap(null);
			});
			
	        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
	        map.setCenter(coords);
			
	    } 
	    
	});  
       };//return
    }(row);//onclick
  }//for
	}

  