
public class Ex03ConsoleInputOutput {

	public static void main(String[] args) {
		
		//1. 키보드 입력
		// 키보드에서 데이터를 읽어들이는 도구 변수 만들기
		java.util.Scanner scanner = new java.util.Scanner(System.in);

		System.out.println("이름을 입력하세요 : "); // Console에 데이터를 출력 ( 끝에 줄바꿈 문자를 출력 )
		String name = scanner.nextLine(); // 키보드에 입력된 데이터를 읽어서 문자열로 변환 -> name 변수에 저장

		System.out.print("나이를 입력하세요 : "); // Console 데이터를 출력 (끝에 줄바꿈 문자를 출력하지 않습니다.)
		int age = scanner.nextInt(); // 키보드에 입력된 데이터를 읽어서 정수로 변환 -> age 변수에 저장

		//2. + 연산자로 문자열 구성 + 모니터 출력
		System.out.println("[" + name + "][" + age + "]");

		//3. format을 사용해서 문자열 구성 + 모니터 출력
		System.out.printf("[%s][%d]\n", name, age); // %s 대신 name의 값을, %d 대신 age의 값을 적용해서 출력
		
		// printf 서식 종류
		// %c : char, %b : boolean, %d : 정수, %f : 실수, %s : 문자열

	}

}
